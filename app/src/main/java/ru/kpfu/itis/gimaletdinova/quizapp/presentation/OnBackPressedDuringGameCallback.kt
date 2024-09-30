package ru.kpfu.itis.gimaletdinova.quizapp.presentation

import androidx.activity.OnBackPressedCallback

class OnBackPressedDuringGameCallback(
    private val onBackPressed: () -> Unit
) : OnBackPressedCallback(true) {
    override fun handleOnBackPressed() {
        onBackPressed.invoke()
    }
}