package ru.kpfu.itis.gimaletdinova.quizapp.util

import android.app.Activity
import android.util.TypedValue

object ResourceUtil {
    fun getColor(resId: Int, activity: Activity): Int {
        val typedValue = TypedValue()
        activity.theme.resolveAttribute(resId, typedValue, true)
        return typedValue.data
    }
}