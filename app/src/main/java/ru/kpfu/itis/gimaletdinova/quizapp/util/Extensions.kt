package ru.kpfu.itis.gimaletdinova.quizapp.util

import android.app.Activity
import android.content.res.Resources
import android.text.Html
import android.util.DisplayMetrics
import android.util.TypedValue
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

fun Int.getValueInPx(dm: DisplayMetrics): Int {
    return (this * dm.density).toInt()
}

inline fun <T> Flow<T>.observe(fragment: Fragment, crossinline block: (T) -> Unit): Job {
    val lifecycleOwner = fragment.viewLifecycleOwner
    return lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            collect { data -> block(data) }
        }
    }
}

fun Activity.getThemeColor(resId: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(resId, typedValue, true)
    return typedValue.data
}

fun String.decodeFromHtml(): String {
    return Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT).toString()
}


