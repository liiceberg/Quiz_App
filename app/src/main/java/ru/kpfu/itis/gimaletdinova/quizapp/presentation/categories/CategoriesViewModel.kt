package ru.kpfu.itis.gimaletdinova.quizapp.presentation.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.gimaletdinova.quizapp.data.ExceptionHandlerDelegate
import ru.kpfu.itis.gimaletdinova.quizapp.data.runCatching
import ru.kpfu.itis.gimaletdinova.quizapp.domain.interactor.GetCategoriesUseCase
import ru.kpfu.itis.gimaletdinova.quizapp.domain.interactor.LevelInteractor
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.categories.model.Category
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val categoriesUseCase: GetCategoriesUseCase,
    private val levelsInteractor: LevelInteractor,
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate,
) : ViewModel() {

    private val _categoriesFlow = MutableStateFlow<List<Category>?>(null)
    val categoriesFlow get() = _categoriesFlow

    private val _loadingFlow = MutableStateFlow(false)
    val loadingFlow get() = _loadingFlow.asStateFlow()
    val errorsChannel = Channel<Throwable>()
    fun getCategories() {
        viewModelScope.launch {
            _loadingFlow.value = true
            runCatching(exceptionHandlerDelegate) {
                categoriesUseCase.invoke()
            }.onSuccess {
                val categoriesList = mutableListOf<Category>()
                for (category in it.categoriesList) {
                    val levelsNumber = levelsInteractor.getNumberByCategory(category.id)
                    categoriesList.add(
                        Category(
                            id = category.id,
                            name = category.displayName,
                            levelsNumber = levelsNumber
                        )
                    )
                }
                _categoriesFlow.value = categoriesList
            }.onFailure { ex ->
                errorsChannel.send(ex)
            }
            _loadingFlow.value = false
        }
    }

    override fun onCleared() {
        errorsChannel.close()
        super.onCleared()
    }
}