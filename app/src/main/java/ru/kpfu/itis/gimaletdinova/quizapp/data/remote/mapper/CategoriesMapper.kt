package ru.kpfu.itis.gimaletdinova.quizapp.data.remote.mapper

import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.response.CategoryData
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.response.CategoryResponse
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.CategoriesList
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.CategoryModel
import ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums.Category
import java.util.stream.Collectors
import javax.inject.Inject

class CategoriesMapper @Inject constructor() {
    fun mapResponseToModel(response: CategoryData): CategoryModel {
        return response.let {
            CategoryModel(
                id = it.id,
                displayName = (it.category ?: Category.OTHER).getName()
            )
        }
    }

    fun mapResponseToCategoriesList(response: CategoryResponse?): CategoriesList? {
        return response?.let {
            CategoriesList(
                categoriesList = response.categories
                    .stream()
                    .map { data -> mapResponseToModel(data) }
                    .collect(Collectors.toList())
            )
        }
    }

}