package ru.kpfu.itis.gimaletdinova.quizapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface QuestionsDao {
    @Query("SELECT count(*) from levels where category_id = :categoryId;")
    fun getLevelsCountByCategory(categoryId: Int): Int

}