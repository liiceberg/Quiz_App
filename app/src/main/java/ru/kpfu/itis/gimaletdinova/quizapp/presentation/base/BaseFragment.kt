package ru.kpfu.itis.gimaletdinova.quizapp.presentation.base

import android.app.AlertDialog
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.util.observe


abstract class BaseFragment(@LayoutRes layout: Int) : Fragment(layout) {

    inline fun <T> Flow<T>.observe(crossinline block: (T) -> Unit): Job {
        return observe(fragment = this@BaseFragment, block)
    }

    fun showError(message: String?, replayAction: (() -> Unit)? = null) {
        with(requireActivity()) {
            val dialogView = this.layoutInflater.inflate(R.layout.error_dialog, null)
            val messageTextView: TextView = dialogView.findViewById(R.id.message_tv)
            messageTextView.text = message

            AlertDialog.Builder(this).apply {

                setView(dialogView)
                    .setNeutralButton(android.R.string.cancel) { dialog, _ ->
                        dialog.cancel()
                    }

                replayAction?.let {
                    this.setPositiveButton(R.string.try_again) { _, _ ->
                        replayAction.invoke()
                    }
                }

                create()
                show()
            }
        }
    }

}