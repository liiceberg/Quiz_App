package ru.kpfu.itis.gimaletdinova.quizapp.data.remote

import ru.kpfu.itis.gimaletdinova.quizapp.data.ExceptionHandlerDelegate
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.response.TriviaResponse
import ru.kpfu.itis.gimaletdinova.quizapp.data.runCatching

class TriviaApiDecorator(
    private val triviaApi: TriviaApi,
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate
) {

    suspend fun getQuestions(amount: Int, category: Int, difficulty: String, type: String)
            : Result<TriviaResponse?> {
        return runCatching(exceptionHandlerDelegate) {
            triviaApi.getQuestions(amount, category, difficulty, type)
        }
    }
}