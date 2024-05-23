package ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.response

import com.google.gson.annotations.SerializedName
import ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums.Category

data class CategoryResponse (
    @SerializedName("trivia_categories")
    val categories: List<CategoryData>
)

data class CategoryData(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val category: Category?
)