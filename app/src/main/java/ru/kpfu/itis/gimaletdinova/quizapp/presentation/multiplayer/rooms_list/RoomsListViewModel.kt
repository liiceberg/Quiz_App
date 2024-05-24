package ru.kpfu.itis.gimaletdinova.quizapp.presentation.multiplayer.rooms_list

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kpfu.itis.gimaletdinova.quizapp.data.ExceptionHandlerDelegate
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.response.Room
import ru.kpfu.itis.gimaletdinova.quizapp.data.runCatching
import ru.kpfu.itis.gimaletdinova.quizapp.domain.interactor.GetCategoriesUseCase
import ru.kpfu.itis.gimaletdinova.quizapp.domain.interactor.RoomInteractor
import ru.kpfu.itis.gimaletdinova.quizapp.domain.interactor.UserInteractor
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.CategoriesList
import javax.inject.Inject

@HiltViewModel
class RoomsListViewModel @Inject constructor(
    private val roomInteractor: RoomInteractor,
    private val userInteractor: UserInteractor,
    private val categoriesUseCase: GetCategoriesUseCase,
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _roomFlow = MutableSharedFlow<List<Room>>()
    val roomFlow get() = _roomFlow
    private var _currentRoomList: List<Room>? = null
    val currentRoomList get() = _currentRoomList
    private val _loadingFlow = MutableStateFlow(false)
    val loadingFlow get() = _loadingFlow.asStateFlow()

    private var viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Main + viewModelJob)
    private var isActive = true
    suspend fun getCategoriesList(): CategoriesList? {
        var categories: CategoriesList? = null
        viewModelScope.async {
            _loadingFlow.value = true
            runCatching(exceptionHandlerDelegate) {
                categoriesUseCase.invoke()
            }.onSuccess {
                categories = it
            }.onFailure { ex ->
//                errorsChannel.send(ex)
            }
            _loadingFlow.value = false
        }.await()
        return categories
    }

    fun getRoomList(allRooms: Boolean) {
        viewModelScope.launch {
            runCatching(exceptionHandlerDelegate) {
                if (allRooms) {
                    roomInteractor.getAll()
                } else {
                    userInteractor.getRooms()
                }
            }.onSuccess {
                roomFlow.emit(it)
                _currentRoomList = it
            }.onFailure { ex ->
//                errorsChannel.send(ex)
            }
        }
    }

    fun <P> doRepeatWork(delay: Long, doOnAsyncBlock: suspend CoroutineScope.() -> P) {
        isActive = true
        viewModelScope.launch {
            while (this@RoomsListViewModel.isActive) {
                withContext(dispatcher) {
                    doOnAsyncBlock.invoke(this)
                }
                if (this@RoomsListViewModel.isActive) {
                    delay(delay)
                }
            }
        }
    }

    fun stopRepeatWork() {
        isActive = false
    }

    override fun onCleared() {
        isActive = false
        viewModelJob.cancel()
    }
}