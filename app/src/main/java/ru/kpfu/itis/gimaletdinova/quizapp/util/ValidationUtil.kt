package ru.kpfu.itis.gimaletdinova.quizapp.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import ru.kpfu.itis.gimaletdinova.quizapp.R

object ValidationUtil {
    fun validateName(context: Context, userInputEt: EditText): Boolean {
        if (userInputEt.text.trim().isEmpty()) {
            userInputEt.error = context.getString(R.string.empty_username_error)
            return false
        }
        if (userInputEt.text.matches(Regex("[A-Za-z]+")).not()) {
            userInputEt.error = context.getString(R.string.incorrect_username_error)
            return false
        }
        return true
    }

    fun hideKeyboard(context: Context?, view: View?) {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}