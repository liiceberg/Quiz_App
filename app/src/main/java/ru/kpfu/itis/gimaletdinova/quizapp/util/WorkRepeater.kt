package ru.kpfu.itis.gimaletdinova.quizapp.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WorkRepeater(private val job: Job, private val dispatcher: CoroutineDispatcher) {

    private val scope = CoroutineScope(Main + job)
    private var isActive = true

    fun doRepeatWork(delay: Long, doOnAsyncBlock: suspend () -> Unit) {
        isActive = true
        scope.launch {
            while (isActive) {
                withContext(dispatcher) {
                    doOnAsyncBlock.invoke()
                }
                if (isActive) {
                    delay(delay)
                }
            }
        }
    }

    fun stopRepeatWork() {
        isActive = false
    }

    fun cancel() {
        isActive = false
        job.cancel()
    }

}