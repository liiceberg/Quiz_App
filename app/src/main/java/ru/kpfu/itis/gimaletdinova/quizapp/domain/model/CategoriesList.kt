package ru.kpfu.itis.gimaletdinova.quizapp.domain.model

data class CategoriesList(
    val categoriesList: List<CategoryModel>
)

data class CategoryModel(
    val id: Int,
    val displayName: String
)