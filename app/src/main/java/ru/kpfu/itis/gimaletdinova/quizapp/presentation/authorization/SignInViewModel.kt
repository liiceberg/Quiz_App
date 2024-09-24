package ru.kpfu.itis.gimaletdinova.quizapp.presentation.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.gimaletdinova.quizapp.data.ExceptionHandlerDelegate
import ru.kpfu.itis.gimaletdinova.quizapp.data.runCatching
import ru.kpfu.itis.gimaletdinova.quizapp.domain.interactor.UserInteractor
import ru.kpfu.itis.gimaletdinova.quizapp.util.Validator
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val userInteractor: UserInteractor,
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate,
    private val validator: Validator
) : ViewModel() {

    private val _loadingFlow = MutableStateFlow(false)
    val loadingFlow get() = _loadingFlow.asStateFlow()

    private val _loggedInFlow = MutableStateFlow(false)
    val loggedInFlow get() = _loggedInFlow.asStateFlow()

    val errorsChannel = Channel<Throwable>()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loadingFlow.value = true
            runCatching(exceptionHandlerDelegate) {
                userInteractor.login(email, password)
            }.onSuccess {
                _loggedInFlow.value = true
            }.onFailure {
                errorsChannel.send(it)
            }
            _loadingFlow.value = false
        }
    }

    fun validateEmail(email: String): Validator.ValidationResult {
        return validator.validateEmail(email)
    }

    override fun onCleared() {
        errorsChannel.close()
    }
}