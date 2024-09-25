package ru.kpfu.itis.gimaletdinova.quizapp.data.remote.mapper

import ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums.Category
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.response.CategoryResponse
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.CategoriesList
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.CategoryModel
import javax.inject.Inject

class CategoriesMapper @Inject constructor() {

    fun mapResponseToCategoriesList(response: CategoryResponse?): CategoriesList? {
        return response?.let {
            CategoriesList(
                categoriesList = response.categories
                    .map { CategoryModel(
                        id = it.id,
                        displayName = (it.category ?: Category.OTHER).getName()
                    ) }
                    .toList()
            )
        }
    }

}