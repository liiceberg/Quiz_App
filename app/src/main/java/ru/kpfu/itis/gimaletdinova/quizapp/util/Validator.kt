package ru.kpfu.itis.gimaletdinova.quizapp.util

import android.content.Context
import android.util.Patterns
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.kpfu.itis.gimaletdinova.quizapp.R
import javax.inject.Inject

class Validator @Inject constructor(
    @ApplicationContext private val context: Context
) {

    class ValidationResult(
        val isValid: Boolean,
        val error: String? = null
    )

    fun validateName(name: String): ValidationResult {
        if (name.trim().isEmpty()) {
            return ValidationResult(false, context.getString(R.string.empty_username_error))
        }
        if (name.matches(Regex("[A-Za-z ]+")).not()) {
            return ValidationResult(false, context.getString(R.string.incorrect_username_error))
        }
        return ValidationResult(true)
    }

    fun validateEmail(email: String) : ValidationResult {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches().not()) {
            return ValidationResult(false, context.getString(R.string.email_error))
        }
        return ValidationResult(true)
    }

    fun validatePassword(password: String) : ValidationResult {
        if (password.matches(Regex("\\w{8,}")).not()) {
            return ValidationResult(false, context.getString(R.string.password_length_error))
        }
        if (password.matches(Regex(".*[A-Z].*")).not()) {
            return ValidationResult(false, context.getString(R.string.password_upper_case_char_error))
        }
        if (password.matches(Regex(".*[a-z].*")).not()) {
            return ValidationResult(false, context.getString(R.string.password_lower_case_char_error))
        }
        if (password.matches(Regex(".*\\d.*")).not()) {
            return ValidationResult(false, context.getString(R.string.password_digit_char_error))
        }
        return ValidationResult(true)
    }

}