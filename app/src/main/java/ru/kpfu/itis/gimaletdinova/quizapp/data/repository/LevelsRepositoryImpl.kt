package ru.kpfu.itis.gimaletdinova.quizapp.data.repository

import ru.kpfu.itis.gimaletdinova.quizapp.data.local.dao.QuestionsDao
import ru.kpfu.itis.gimaletdinova.quizapp.data.local.entity.LevelEntity
import ru.kpfu.itis.gimaletdinova.quizapp.data.local.entity.QuestionEntity
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.QuestionsList
import ru.kpfu.itis.gimaletdinova.quizapp.domain.repository.LevelsRepository
import javax.inject.Inject

class LevelsRepositoryImpl @Inject constructor(
    private val questionsDao: QuestionsDao
) : LevelsRepository {
    override suspend fun getNumberByCategory(id: Int): Int {
        return questionsDao.getLevelsCountByCategory(id)
    }

    override suspend fun saveQuestions(number: Int, categoryId: Int, questions: QuestionsList) {
        var levelId = questionsDao.getLevelId(categoryId, number)
        if (levelId == null) {
            questionsDao.createLevel(LevelEntity(categoryId = categoryId, number = number))
            levelId = questionsDao.getLevelId(categoryId, number)
        }

        for ((ind, q) in questions.questions.withIndex()) {
            questionsDao.saveQuestion(
                QuestionEntity(
                    number = ind + 1,
                    question = q.question,
                    answers = q.answers,
                    correctAnswerPosition = q.correctAnswerPosition,
                    userAnswerPosition = q.userAnswerPosition,
                    levelId = levelId
                )
            )
        }
    }

    override suspend fun getSavedQuestions(number: Int, categoryId: Int) : List<QuestionEntity>? {
        val id = questionsDao.getLevelId(categoryId, number)
        println(id)
        return questionsDao.getQuestions(id)
    }
}