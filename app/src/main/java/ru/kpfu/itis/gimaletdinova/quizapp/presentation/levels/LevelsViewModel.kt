package ru.kpfu.itis.gimaletdinova.quizapp.presentation.levels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums.LevelDifficulty
import ru.kpfu.itis.gimaletdinova.quizapp.domain.repository.LevelsRepository
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.levels.model.Difficulty
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.levels.model.Item
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.levels.model.Level
import ru.kpfu.itis.gimaletdinova.quizapp.util.Constants
import ru.kpfu.itis.gimaletdinova.quizapp.util.Constants.EASY_LEVELS_NUMBER
import ru.kpfu.itis.gimaletdinova.quizapp.util.Constants.MEDIUM_LEVELS_NUMBER
import javax.inject.Inject

@HiltViewModel
class LevelsViewModel @Inject constructor(
    private val levelsRepository: LevelsRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    val levelsList = mutableListOf<Item>()

    private val _loadingFlow = MutableStateFlow(false)
    val loadingFlow get() = _loadingFlow.asStateFlow()
    fun getLevels(id: Int) {
        viewModelScope.launch {
            _loadingFlow.value = true
            withContext(dispatcher) {
                val levelsNumber = levelsRepository.getNumberByCategory(id)
                levelsList.add(Difficulty(LevelDifficulty.EASY.name))
                for (i in 1..Constants.LEVELS_NUMBER) {
                    when (i - 1) {
                        EASY_LEVELS_NUMBER ->
                            levelsList.add(Difficulty(LevelDifficulty.MEDIUM.name))
                        EASY_LEVELS_NUMBER + MEDIUM_LEVELS_NUMBER ->
                            levelsList.add(Difficulty(LevelDifficulty.HARD.name))
                    }
                    levelsList.add(
                        Level(number = i,
                            isBlocked = i > levelsNumber + 1,
                            difficulty = LevelDifficulty.get(i))
                    )
                }
            }
            _loadingFlow.value = false
        }
    }
}