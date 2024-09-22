package ru.kpfu.itis.gimaletdinova.quizapp.presentation.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.kpfu.itis.gimaletdinova.quizapp.data.ExceptionHandlerDelegate
import ru.kpfu.itis.gimaletdinova.quizapp.data.runCatching
import ru.kpfu.itis.gimaletdinova.quizapp.domain.interactor.UserInteractor
import ru.kpfu.itis.gimaletdinova.quizapp.util.Validator
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val userInteractor: UserInteractor,
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate,
    private val validator: Validator
) : ViewModel() {

    private val _loadingFlow = MutableStateFlow(false)
    val loadingFlow get() = _loadingFlow.asStateFlow()
    val errorsChannel = Channel<Throwable>()

    suspend fun save(email: String, password: String) : Boolean {
        var registered = false
        viewModelScope.async {
            _loadingFlow.value = true
            runCatching(exceptionHandlerDelegate) {
                userInteractor.register(email, password)
            }.onSuccess {
                registered = true
            }.onFailure {
                errorsChannel.send(it)
            }
        }.await()
        _loadingFlow.value = false
        return registered
    }

    fun validateEmail(email: String) : Validator.ValidationResult {
        return validator.validateEmail(email)
    }

    fun validatePassword(password: String) : Validator.ValidationResult {
        return validator.validatePassword(password)
    }

    override fun onCleared() {
        errorsChannel.close()
    }
}