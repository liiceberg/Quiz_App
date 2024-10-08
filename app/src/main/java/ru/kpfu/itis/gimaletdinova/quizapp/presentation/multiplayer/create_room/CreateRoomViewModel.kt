package ru.kpfu.itis.gimaletdinova.quizapp.presentation.multiplayer.create_room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.gimaletdinova.quizapp.data.ExceptionHandlerDelegate
import ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums.LevelDifficulty
import ru.kpfu.itis.gimaletdinova.quizapp.data.runCatching
import ru.kpfu.itis.gimaletdinova.quizapp.domain.interactor.GetCategoriesUseCase
import ru.kpfu.itis.gimaletdinova.quizapp.domain.interactor.RoomInteractor
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.CategoriesList
import javax.inject.Inject

@HiltViewModel
class CreateRoomViewModel @Inject constructor(
    private val categoriesUseCase: GetCategoriesUseCase,
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate,
    private val roomInteractor: RoomInteractor
) : ViewModel() {

    private val _categoriesFlow = MutableStateFlow<CategoriesList?>(null)
    val categoriesFlow get() = _categoriesFlow

    private val _loadingFlow = MutableStateFlow(false)
    val loadingFlow get() = _loadingFlow.asStateFlow()

    private val _createRoomFlow = MutableStateFlow<String?>(null)
    val createRoomFlow get() = _createRoomFlow.asStateFlow()

    val errorsChannel = Channel<Throwable>()

    fun create(playersNumber: Int, categoryId: Int?, difficulty: LevelDifficulty?) {
        viewModelScope.launch {
            _loadingFlow.value = true
            runCatching(exceptionHandlerDelegate) {
                roomInteractor.createRoom(playersNumber, categoryId, difficulty)
            }.onSuccess {
                _createRoomFlow.value = it
            }.onFailure {
                errorsChannel.send(it)
            }
            _loadingFlow.value = false
        }
    }

    fun getCategoriesList() {
        viewModelScope.launch {
            _loadingFlow.value = true
            runCatching(exceptionHandlerDelegate) {
                categoriesUseCase.invoke()
            }.onSuccess {
                _categoriesFlow.value = it
            }.onFailure { ex ->
                errorsChannel.send(ex)
            }
            _loadingFlow.value = false
        }
    }

    override fun onCleared() {
        errorsChannel.close()
    }
}