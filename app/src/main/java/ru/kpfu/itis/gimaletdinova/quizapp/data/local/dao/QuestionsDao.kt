package ru.kpfu.itis.gimaletdinova.quizapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.kpfu.itis.gimaletdinova.quizapp.data.local.entity.LevelEntity
import ru.kpfu.itis.gimaletdinova.quizapp.data.local.entity.QuestionEntity

@Dao
interface QuestionsDao {
    @Query("SELECT count(*) from levels where category_id = :categoryId and user_id = :userId;")
    fun getLevelsCountByCategory(categoryId: Int, userId: Long): Int
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun createLevel(level: LevelEntity)
    @Query("SELECT id from levels where category_id = :categoryId and number = :levelNumber and user_id = :userId;")
    fun getLevelId(categoryId: Int, levelNumber: Int, userId: Long) : Int?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveQuestion(entity: QuestionEntity)
    @Query("SELECT * from questions where level_id = :levelId order by number")
    fun getQuestions(levelId: Int?) : List<QuestionEntity>?

}