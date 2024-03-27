package ru.kpfu.itis.gimaletdinova.quizapp.util

import android.util.DisplayMetrics

fun Int.getValueInPx(dm: DisplayMetrics): Int {
    return (this * dm.density).toInt()
}
