package ru.kpfu.itis.gimaletdinova.quizapp.presentation.multiplayer.rooms_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
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
import ru.kpfu.itis.gimaletdinova.quizapp.util.WorkRepeater
import javax.inject.Inject

@HiltViewModel
class RoomsListViewModel @Inject constructor(
    private val roomInteractor: RoomInteractor,
    private val userInteractor: UserInteractor,
    private val categoriesUseCase: GetCategoriesUseCase,
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate,
    private val workRepeater: WorkRepeater
) : ViewModel() {

    private val _roomFlow = MutableStateFlow<List<Room>?>(null)
    val roomFlow get() = _roomFlow

    private val _loadingFlow = MutableStateFlow(false)
    val loadingFlow get() = _loadingFlow.asStateFlow()

    private val _categoriesFlow = MutableStateFlow<CategoriesList?>(null)
    val categoriesFlow get() = _categoriesFlow.asStateFlow()

    val errorsChannel = Channel<Throwable>()

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
        val function = if (allRooms) {
            ::getAllRooms
        } else {
            ::getUserRooms
        }
        workRepeater.doRepeatWork(REPEAT_CHECKING_INTERVAL, function)
    }

    private suspend inline fun getAllRooms() {
        runCatching(exceptionHandlerDelegate) {
            roomInteractor.getAll()
        }.onSuccess {
            _roomFlow.value = it
        }.onFailure { ex ->
            stopRepeatWork()
            errorsChannel.send(ex)
            _roomFlow.value = emptyList()
        }
    }

    private suspend inline fun getUserRooms() {
        runCatching(exceptionHandlerDelegate) {
            userInteractor.getRooms()
        }.onSuccess {
            _roomFlow.value = it
        }.onFailure { ex ->
            stopRepeatWork()
            errorsChannel.send(ex)
            _roomFlow.value = emptyList()
        }
    }

    fun stopRepeatWork() {
        workRepeater.stopRepeatWork()
    }

    override fun onCleared() {
        workRepeater.cancel()
        errorsChannel.close()
    }

    companion object {
        private const val REPEAT_CHECKING_INTERVAL = 1000L
    }
}