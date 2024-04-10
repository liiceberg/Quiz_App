package ru.kpfu.itis.gimaletdinova.quizapp.data.remote.mapper

import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.response.TriviaResponse
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.QuestionModel
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.QuestionsList
import ru.kpfu.itis.gimaletdinova.quizapp.util.decodeFromHtml
import ru.kpfu.itis.gimaletdinova.quizapp.util.enums.Category
import ru.kpfu.itis.gimaletdinova.quizapp.util.enums.exception.CategoryNotFoundException
import ru.kpfu.itis.gimaletdinova.quizapp.util.enums.LevelDifficulty
import ru.kpfu.itis.gimaletdinova.quizapp.util.enums.exception.LevelDifficultyNotFoundException
import ru.kpfu.itis.gimaletdinova.quizapp.util.enums.QuestionType
import ru.kpfu.itis.gimaletdinova.quizapp.util.enums.exception.QuestionTypeNotFoundException
import javax.inject.Inject

class QuestionsListMapper @Inject constructor() {
    fun mapResponseToQuestionsList(response: TriviaResponse?): QuestionsList? {
        return response?.questions?.let { list ->
            val questionsList = mutableListOf<QuestionModel>()
            for (i in list.indices) {
                with(list[i]) {
                    questionsList.add(QuestionModel(
                        number = i + 1,
                        type = mapQuestionType(this.type),
                        difficulty = mapDifficulty(this.difficulty),
                        category = mapCategory(this.category),
                        question = this.question?.decodeFromHtml() ?: "",
                        correctAnswer = this.correctAnswer?.decodeFromHtml() ?: "",
                        incorrectAnswers = this.incorrectAnswers?.map { s -> s.decodeFromHtml() }
                            ?: listOf()
                    ))
                }
            }
            return QuestionsList(questions = questionsList)
        }
    }

    private fun mapQuestionType(type: String?): QuestionType {
        return when (type) {
            "multiple" -> QuestionType.MULTIPLE_CHOICE
            else -> throw QuestionTypeNotFoundException()
        }
    }

    private fun mapDifficulty(difficulty: String?): LevelDifficulty {
        return when (difficulty) {
            "easy" -> LevelDifficulty.EASY
            "medium" -> LevelDifficulty.MEDIUM
            "hard" -> LevelDifficulty.HARD
            else -> throw LevelDifficultyNotFoundException()
        }
    }

    private fun mapCategory(name: String?): Category {
        return when (name) {
            "General Knowledge" -> Category.GENERAL_KNOWLEDGE
            "Entertainment: Books" -> Category.BOOKS
            "Entertainment: Film" -> Category.FILMS
            "Entertainment: Music" -> Category.MUSIC
            "Entertainment: Musicals &amp; Theatres" -> Category.MUSICALS_AND_THEATRES
            "Entertainment: Television" -> Category.TELEVISION
            "Entertainment: Video Games" -> Category.VIDEO_GAMES
            "Entertainment: Board Games" -> Category.BOARD_GAMES
            "Science &amp; Nature" -> Category.SCIENCE_AND_NATURE
            "Science: Computers" -> Category.COMPUTERS
            "Science: Mathematics" -> Category.MATHEMATICS
            "Mythology" -> Category.MYTHOLOGY
            "Sports" -> Category.SPORTS
            "Geography" -> Category.GEOGRAPHY
            "History" -> Category.HISTORY
            "Politics" -> Category.POLITICS
            "Art" -> Category.ART
            "Celebrities" -> Category.CELEBRITIES
            "Animals" -> Category.ANIMALS
            "Vehicles" -> Category.VEHICLES
            "Entertainment: Comics" -> Category.COMICS
            "Science: Gadgets" -> Category.GADGETS
            "Entertainment: Japanese Anime &amp; Manga" -> Category.ANIME_AND_MANGA
            "Entertainment: Cartoon &amp; Animations" -> Category.CARTOON_AND_ANIMATIONS

            else -> throw CategoryNotFoundException()
        }
    }
}