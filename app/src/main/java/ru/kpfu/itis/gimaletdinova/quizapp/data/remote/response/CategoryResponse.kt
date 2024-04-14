package ru.kpfu.itis.gimaletdinova.quizapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class CategoryResponse (
    @SerializedName("trivia_categories")
    val categories: List<CategoryData>
)

data class CategoryData(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)