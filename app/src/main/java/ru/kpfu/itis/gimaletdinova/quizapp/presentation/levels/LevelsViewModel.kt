package ru.kpfu.itis.gimaletdinova.quizapp.presentation.levels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums.LevelDifficulty
import ru.kpfu.itis.gimaletdinova.quizapp.domain.interactor.LevelInteractor
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.levels.model.Difficulty
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.levels.model.Item
import ru.kpfu.itis.gimaletdinova.quizapp.presentation.levels.model.Level
import ru.kpfu.itis.gimaletdinova.quizapp.util.GameConfigConstants
import ru.kpfu.itis.gimaletdinova.quizapp.util.GameConfigConstants.EASY_LEVELS_NUMBER
import ru.kpfu.itis.gimaletdinova.quizapp.util.GameConfigConstants.MEDIUM_LEVELS_NUMBER
import javax.inject.Inject

@HiltViewModel
class LevelsViewModel @Inject constructor(
    private val levelInteractor: LevelInteractor,
) : ViewModel() {

    private val _levelsFlow = MutableStateFlow<List<Item>?>(null)
    val levelsFlow get() = _levelsFlow

    private val _loadingFlow = MutableStateFlow(false)
    val loadingFlow get() = _loadingFlow.asStateFlow()

    private var _levelsNumber: Int = -1
    val levelsNumber get() = _levelsNumber

    fun getLevels(id: Int) {
        viewModelScope.launch {
            _loadingFlow.value = true
            _levelsNumber = levelInteractor.getNumberByCategory(id)
            val levelsList = mutableListOf<Item>()
            levelsList.add(Difficulty(LevelDifficulty.EASY.name))
            for (i in 1..GameConfigConstants.LEVELS_NUMBER) {
                when (i - 1) {
                    EASY_LEVELS_NUMBER ->
                        levelsList.add(Difficulty(LevelDifficulty.MEDIUM.name))

                    EASY_LEVELS_NUMBER + MEDIUM_LEVELS_NUMBER ->
                        levelsList.add(Difficulty(LevelDifficulty.HARD.name))
                }
                levelsList.add(
                    Level(
                        number = i,
                        isBlocked = i > levelsNumber + 1,
                        difficulty = LevelDifficulty.get(i)
                    )
                )
            }
            _levelsFlow.value = levelsList

            _loadingFlow.value = false
        }
    }
}