package ru.kpfu.itis.gimaletdinova.quizapp.data.remote.mapper

import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.response.TriviaResponse
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.QuestionModel
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.QuestionsList
import ru.kpfu.itis.gimaletdinova.quizapp.util.decodeFromHtml
import javax.inject.Inject

class QuestionsListMapper @Inject constructor() {
    fun mapResponseToQuestionsList(response: TriviaResponse?): QuestionsList? {
        return response?.questions?.let { list ->
            val questionsList = mutableListOf<QuestionModel>()
            for (i in list.indices) {
                with(list[i]) {
                    val answers = getAnswers(this.correctAnswer, this.incorrectAnswers)
                    questionsList.add( QuestionModel(
                        number = i + 1,
                        type = this.type,
                        difficulty = this.difficulty,
                        question = this.question.decodeFromHtml(),
                        correctAnswerPosition = answers.indexOf(this.correctAnswer),
                        answers = answers.map { s -> s.decodeFromHtml() }
                    ))
                }
            }
            return QuestionsList(questions = questionsList)
        }
    }

    private fun getAnswers(correctAnswer: String, incorrectAnswers: List<String>): List<String> {
        val list = mutableListOf<String>()
        list.add(correctAnswer)
        list.addAll(incorrectAnswers)
        list.shuffle()
        return list
    }

}