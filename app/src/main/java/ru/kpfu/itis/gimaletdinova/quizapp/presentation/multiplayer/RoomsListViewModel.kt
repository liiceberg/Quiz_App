package ru.kpfu.itis.gimaletdinova.quizapp.presentation.multiplayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.gimaletdinova.quizapp.data.ExceptionHandlerDelegate
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.response.Room
import ru.kpfu.itis.gimaletdinova.quizapp.data.runCatching
import ru.kpfu.itis.gimaletdinova.quizapp.domain.interactor.GetCategoriesUseCase
import ru.kpfu.itis.gimaletdinova.quizapp.domain.interactor.RoomInteractor
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.CategoriesList
import javax.inject.Inject

@HiltViewModel
class RoomsListViewModel @Inject constructor(
    private val roomInteractor: RoomInteractor,
    private val categoriesUseCase: GetCategoriesUseCase,
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate
): ViewModel() {
    
    private val _roomFlow = MutableSharedFlow<List<Room>>()
    val roomFlow get() = _roomFlow
    private var _currentRoomList : List<Room>? = null
    val currentRoomList get() = _currentRoomList
    private val _loadingFlow = MutableStateFlow(false)
    val loadingFlow get() = _loadingFlow.asStateFlow()
    suspend fun getCategoriesList() : CategoriesList? {
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

    fun getRoomList() {
        viewModelScope.launch {
            _loadingFlow.value = true
            runCatching(exceptionHandlerDelegate) {
                roomInteractor.getAll()
            }.onSuccess {
                roomFlow.emit(it)
                _currentRoomList = it
            }.onFailure { ex ->
//                errorsChannel.send(ex)
            }
            _loadingFlow.value = false
        }
    }
}