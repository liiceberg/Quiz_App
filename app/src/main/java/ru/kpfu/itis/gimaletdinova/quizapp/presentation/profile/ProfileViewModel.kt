package ru.kpfu.itis.gimaletdinova.quizapp.presentation.profile


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.data.ExceptionHandlerDelegate
import ru.kpfu.itis.gimaletdinova.quizapp.data.runCatching
import ru.kpfu.itis.gimaletdinova.quizapp.domain.interactor.ChangeThemeInteractor
import ru.kpfu.itis.gimaletdinova.quizapp.domain.interactor.ScoreInteractor
import ru.kpfu.itis.gimaletdinova.quizapp.domain.interactor.UserInteractor
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.UserScores
import ru.kpfu.itis.gimaletdinova.quizapp.util.Validator
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userInteractor: UserInteractor,
    private val changeThemeInteractor: ChangeThemeInteractor,
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate,
    private val scoreInteractor: ScoreInteractor,
    private val validator: Validator,
    @ApplicationContext private val context: Context
) : ViewModel() {

    init {
        initProfile()
    }

    private val _usernameFlow = MutableStateFlow("")
    val usernameFlow get() = _usernameFlow.asStateFlow()

    private val _scoresFlow = MutableStateFlow<UserScores?>(null)
    val scoresFlow get() = _scoresFlow.asStateFlow()

    val themeFlow = changeThemeInteractor.themeFlow()

    private val _loggedOutFlow = MutableStateFlow(false)
    val loggedOutFlow get() = _loggedOutFlow.asStateFlow()

    val errorsChannel = Channel<Throwable>()

    fun initProfile() {
        viewModelScope.launch {
            getUsername()
            getScores()
        }
    }

    private suspend fun getUsername() {
        runCatching(exceptionHandlerDelegate) {
            userInteractor.getUsername()
        }.onSuccess {
            _usernameFlow.value = it ?: context.getString(R.string.default_username)
        }.onFailure {
            errorsChannel.send(it)
        }
    }

    private suspend fun getScores() {
        runCatching(exceptionHandlerDelegate) {
            scoreInteractor.getScores()
        }.onSuccess {
            _scoresFlow.value = it ?: UserScores()
        }.onFailure {
            errorsChannel.send(it)
        }
    }

    fun saveUsername(name: String) {
        viewModelScope.launch {
            runCatching(exceptionHandlerDelegate) {
                userInteractor.setUsername(name)
            }.onSuccess {
                _usernameFlow.value = name
            }.onFailure {
                errorsChannel.send(it)
            }
        }
    }

    fun validateUsername(username: String): Validator.ValidationResult {
        return validator.validateName(username)
    }

    fun changeTheme() {
        viewModelScope.launch {
            changeThemeInteractor.changeTheme()
        }
    }

    fun logout() {
        viewModelScope.launch {
            userInteractor.logout()
            _loggedOutFlow.value = true
        }
    }

    override fun onCleared() {
        errorsChannel.close()
    }
}