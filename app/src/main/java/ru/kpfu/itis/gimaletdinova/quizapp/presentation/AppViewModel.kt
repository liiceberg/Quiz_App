package ru.kpfu.itis.gimaletdinova.quizapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.domain.interactor.ChangeThemeInteractor
import ru.kpfu.itis.gimaletdinova.quizapp.domain.interactor.UserInteractor
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val userInteractor: UserInteractor,
    private val changeThemeInteractor: ChangeThemeInteractor
) : ViewModel() {

    fun setTheme() {
        viewModelScope.launch {
            changeThemeInteractor.setCurrentTheme()
        }
    }

    suspend fun getStartDestination(): Int {
        return try {
            userInteractor.getUserId()
            R.id.startFragment
        } catch (ex: Exception) {
            R.id.signInFragment
        }
    }

}