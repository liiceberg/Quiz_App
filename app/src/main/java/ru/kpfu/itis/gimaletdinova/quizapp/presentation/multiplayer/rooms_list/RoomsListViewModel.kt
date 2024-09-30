package ru.kpfu.itis.gimaletdinova.quizapp.presentation.multiplayer.rooms_list

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate
) : ViewModel() {

    private val _roomFlow = MutableStateFlow<List<Room>>(emptyList())
    val roomFlow get() = _roomFlow

    private val _loadingFlow = MutableStateFlow(false)
    val loadingFlow get() = _loadingFlow.asStateFlow()

    private val _categoriesFlow = MutableStateFlow<CategoriesList?>(null)
    val categoriesFlow get() = _categoriesFlow.asStateFlow()

    val errorsChannel = Channel<Throwable>()

    private var viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Main + viewModelJob)
    private var isActive = true

    fun getCategoriesList() {
        viewModelScope.launch {
            _loadingFlow.value = true
            runCatching(exceptionHandlerDelegate) {
                categoriesUseCase.invoke()
            }.onSuccess {
                _categoriesFlow.value = it
            }.onFailure {
                _categoriesFlow.value = CategoriesList.empty()
            }
            _loadingFlow.value = false
        }
    }

    fun repeatCheckingRoomsForUpdates(allRooms: Boolean) {
        isActive = true
        viewModelScope.launch {
            while (this@RoomsListViewModel.isActive) {
                getRoomList(allRooms)
                if (this@RoomsListViewModel.isActive) {
                    delay(REPEAT_CHECKING_INTERVAL)
                }
            }
        }
    }

    private suspend fun getRoomList(allRooms: Boolean) {
        runCatching(exceptionHandlerDelegate) {
            if (allRooms) {
                roomInteractor.getAll()
            } else {
                userInteractor.getRooms()
            }
        }.onSuccess {
            _roomFlow.value = it
        }.onFailure { ex ->
            stopRepeatWork()
            errorsChannel.send(ex)
            _roomFlow.value = emptyList()
        }
    }

    fun stopRepeatWork() {
        isActive = false
    }

    override fun onCleared() {
        stopRepeatWork()
        viewModelJob.cancel()
        errorsChannel.close()
    }

    companion object {
        private const val REPEAT_CHECKING_INTERVAL = 1000L
    }
}