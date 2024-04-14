package ru.kpfu.itis.gimaletdinova.quizapp.data.remote.mapper

import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.response.CategoryData
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.response.CategoryResponse
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.CategoriesList
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.CategoryModel
import ru.kpfu.itis.gimaletdinova.quizapp.util.enums.Category
import java.util.stream.Collectors
import javax.inject.Inject

class CategoriesMapper @Inject constructor() {
    fun mapResponseToModel(response: CategoryData): CategoryModel {
        return response.let {
            val category = mapCategory(it.name)
            CategoryModel(
                id = it.id,
                displayName = if (category != Category.OTHER) category.getName() else it.name
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

    private fun mapCategory(name: String): Category {
        return when (name) {
            "General Knowledge" -> Category.GENERAL_KNOWLEDGE
            "Entertainment: Books" -> Category.BOOKS
            "Entertainment: Film" -> Category.FILMS
            "Entertainment: Music" -> Category.MUSIC
            "Entertainment: Musicals & Theatres" -> Category.MUSICALS_AND_THEATRES
            "Entertainment: Television" -> Category.TELEVISION
            "Entertainment: Video Games" -> Category.VIDEO_GAMES
            "Entertainment: Board Games" -> Category.BOARD_GAMES
            "Science &amp; Nature" -> Category.SCIENCE_AND_NATURE
            "Science: Computers" -> Category.COMPUTERS
            "Science: Mathematics" -> Category.MATHEMATICS
            "Mythology" -> Category.MYTHOLOGY
            "Sports" -> Category.SPORTS
            "Geography" -> Category.GEOGRAPHY
            "History" -> Category.HISTORY
            "Politics" -> Category.POLITICS
            "Art" -> Category.ART
            "Celebrities" -> Category.CELEBRITIES
            "Animals" -> Category.ANIMALS
            "Vehicles" -> Category.VEHICLES
            "Entertainment: Comics" -> Category.COMICS
            "Science: Gadgets" -> Category.GADGETS
            "Entertainment: Japanese Anime & Manga" -> Category.ANIME_AND_MANGA
            "Entertainment: Cartoon & Animations" -> Category.CARTOON_AND_ANIMATIONS
            else -> Category.OTHER
        }
    }
}