package ru.kpfu.itis.gimaletdinova.quizapp.presentation.multiplayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.gimaletdinova.quizapp.data.ExceptionHandlerDelegate
import ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums.LevelDifficulty
import ru.kpfu.itis.gimaletdinova.quizapp.data.runCatching
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.CategoriesList
import ru.kpfu.itis.gimaletdinova.quizapp.domain.usecase.GetCategoriesUseCase
import ru.kpfu.itis.gimaletdinova.quizapp.domain.usecase.RoomInteractor
import javax.inject.Inject

@HiltViewModel
class CreateRoomViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate,
    private val roomInteractor: RoomInteractor
): ViewModel() {
    private val _categoriesFlow = MutableStateFlow<CategoriesList?>(null)
    val categoriesFlow get() = _categoriesFlow


    private val _loadingFlow = MutableStateFlow(false)
    val loadingFlow get() = _loadingFlow.asStateFlow()
    val errorsChannel = Channel<Throwable>()
    suspend fun create(playersNumber: Int, categoryId: Int?, difficulty: LevelDifficulty?) : String? {
        var code : String? = null
        viewModelScope.async {
            _loadingFlow.value = true
            runCatching(exceptionHandlerDelegate) {
                roomInteractor.createRoom(playersNumber, categoryId, difficulty)
            }.onSuccess {
                code = it
            }
        }.await()
        _loadingFlow.value = false
        return code
    }

    fun getCategoriesList() {
        viewModelScope.launch {
            _loadingFlow.value = true
            runCatching(exceptionHandlerDelegate) {
                getCategoriesUseCase.invoke()
            }.onSuccess {
                _categoriesFlow.value = it
            }.onFailure { ex ->
                errorsChannel.send(ex)
            }
            _loadingFlow.value = false
        }
    }
}