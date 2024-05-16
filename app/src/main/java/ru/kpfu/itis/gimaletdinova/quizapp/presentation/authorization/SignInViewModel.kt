package ru.kpfu.itis.gimaletdinova.quizapp.presentation.authorization

import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kpfu.itis.gimaletdinova.quizapp.data.ExceptionHandlerDelegate
import ru.kpfu.itis.gimaletdinova.quizapp.data.runCatching
import ru.kpfu.itis.gimaletdinova.quizapp.domain.repository.UserRepository
import ru.kpfu.itis.gimaletdinova.quizapp.util.PrefsKeys
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate,
    private val prefs: DataStore<Preferences>,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _loadingFlow = MutableStateFlow(false)
    val loadingFlow get() = _loadingFlow.asStateFlow()
    suspend fun login(email: String, password: String) : Boolean {
        var loggedIn = false
        viewModelScope.async {
            _loadingFlow.value = true
            runCatching(exceptionHandlerDelegate) {
                val id = userRepository.login(email, password)
                saveUser(id)
            }.onSuccess {
                loggedIn = true
            }
        }.await()
        _loadingFlow.value = false
        return loggedIn
    }

    private fun saveUser(id: Long) {
        viewModelScope.launch {
            withContext(dispatcher) {
                prefs.edit {
                    it[PrefsKeys.USER_ID_KEY] = id
                }
            }
        }
    }
}