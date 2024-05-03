package ru.kpfu.itis.gimaletdinova.quizapp.data.remote.mapper

import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.response.CategoryResponse
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.CategoriesList
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.CategoryModel
import ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums.Category
import java.util.stream.Collectors
import javax.inject.Inject

class CategoriesMapper @Inject constructor() {

    fun mapResponseToCategoriesList(response: CategoryResponse?): CategoriesList? {
        return response?.let {
            CategoriesList(
                categoriesList = response.categories
                    .stream()
                    .map { CategoryModel(
                        id = it.id,
                        displayName = (it.category ?: Category.OTHER).getName()
                    ) }
                    .collect(Collectors.toList())
            )
        }
    }

}