package ru.kpfu.itis.gimaletdinova.quizapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.kpfu.itis.gimaletdinova.quizapp.data.local.entity.ScoreEntity

@Dao
interface ScoresDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveScore(score: ScoreEntity)
    @Query("select * from scores where user_id = :userId")
    fun getScore(userId: Long) : ScoreEntity?

}