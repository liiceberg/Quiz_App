package ru.kpfu.itis.gimaletdinova.quizapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "scores", indices = [Index(value = ["user_id"], unique = true)]
)
data class ScoreEntity (
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "user_id") val userId: Long,
    @ColumnInfo(name = "user_scores_number") var userScoresNumber: Int,
    @ColumnInfo(name = "total_scores_number") var totalScoresNumber: Int
)