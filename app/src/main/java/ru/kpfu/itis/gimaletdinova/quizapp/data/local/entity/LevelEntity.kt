package ru.kpfu.itis.gimaletdinova.quizapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "levels", indices = [Index(value = ["category_id", "number"], unique = true)])
data class LevelEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "category_id") val categoryId: Int,
    val number: Int
)