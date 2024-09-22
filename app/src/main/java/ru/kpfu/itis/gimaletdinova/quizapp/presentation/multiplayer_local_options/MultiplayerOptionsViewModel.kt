package ru.kpfu.itis.gimaletdinova.quizapp.presentation.multiplayer_local_options

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.kpfu.itis.gimaletdinova.quizapp.util.Validator
import javax.inject.Inject

@HiltViewModel
class MultiplayerOptionsViewModel @Inject constructor(
    private val validator: Validator
) : ViewModel() {
    fun validateUsername(name: String) : Validator.ValidationResult {
        return validator.validateName(name)
    }
}