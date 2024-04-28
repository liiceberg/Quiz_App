package ru.kpfu.itis.gimaletdinova.quizapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "questions",
    foreignKeys = [ForeignKey(
        entity = LevelEntity::class,
        parentColumns = ["id"],
        childColumns = ["level_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["level_id"])]
)
data class QuestionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val number: Int,
    val question: String,
    val answers: List<String>,
    val correctAnswerPosition: Int,
    val userAnswerPosition: Int,
    @ColumnInfo(name = "level_id") val levelId: Int?
)