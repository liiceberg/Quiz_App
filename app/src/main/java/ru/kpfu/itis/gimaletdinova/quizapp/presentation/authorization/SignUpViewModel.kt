package ru.kpfu.itis.gimaletdinova.quizapp.presentation.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.kpfu.itis.gimaletdinova.quizapp.data.ExceptionHandlerDelegate
import ru.kpfu.itis.gimaletdinova.quizapp.data.runCatching
import ru.kpfu.itis.gimaletdinova.quizapp.domain.interactor.UserInteractor
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val userInteractor: UserInteractor,
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate,
) : ViewModel() {
    private val _loadingFlow = MutableStateFlow(false)
    val loadingFlow get() = _loadingFlow.asStateFlow()
    suspend fun save(email: String, password: String) : Boolean {
        var registered = false
        viewModelScope.async {
            _loadingFlow.value = true
            runCatching(exceptionHandlerDelegate) {
                userInteractor.register(email, password)
            }.onSuccess {
                registered = true
            }
        }.await()
        _loadingFlow.value = false
        return registered
    }
}