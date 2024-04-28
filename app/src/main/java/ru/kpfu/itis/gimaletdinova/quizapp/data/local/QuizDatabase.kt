package ru.kpfu.itis.gimaletdinova.quizapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.kpfu.itis.gimaletdinova.quizapp.data.local.dao.QuestionsDao
import ru.kpfu.itis.gimaletdinova.quizapp.data.local.entity.LevelEntity
import ru.kpfu.itis.gimaletdinova.quizapp.data.local.entity.QuestionEntity

@Database(entities = [QuestionEntity::class, LevelEntity::class], version = 1)
@TypeConverters(Converter::class)
abstract class QuizDatabase : RoomDatabase() {
    abstract val questionsDao: QuestionsDao
}