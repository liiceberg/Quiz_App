package ru.kpfu.itis.gimaletdinova.quizapp.presentation.results_view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kpfu.itis.gimaletdinova.quizapp.data.local.entity.QuestionEntity
import ru.kpfu.itis.gimaletdinova.quizapp.domain.repository.LevelsRepository
import javax.inject.Inject

@HiltViewModel
class ResultsViewViewModel @Inject constructor(
    private val levelsRepository: LevelsRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private var _savedQuestions : List<QuestionEntity>? = null
    val savedQuestions get() = _savedQuestions

    private val _loadingFlow = MutableStateFlow(false)
    val loadingFlow get() = _loadingFlow.asStateFlow()

    fun getQuestions(levelNumber: Int, categoryId: Int) {
        viewModelScope.launch {
            _loadingFlow.value = true
            withContext(dispatcher) {
                _savedQuestions = levelsRepository.getSavedQuestions(levelNumber, categoryId)
            }
            _loadingFlow.value = false
        }
    }

}