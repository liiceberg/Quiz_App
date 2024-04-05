package ru.kpfu.itis.gimaletdinova.quizapp.util

object ValidationUtil {
    fun validateName(name: String): Boolean {
        return !(name.trim().isEmpty() || name.matches(Regex("[A-Za-z]+")).not())
    }

}