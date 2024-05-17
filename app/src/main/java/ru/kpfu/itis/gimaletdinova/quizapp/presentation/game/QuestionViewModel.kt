package ru.kpfu.itis.gimaletdinova.quizapp.presentation.game

import android.os.CountDownTimer
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kpfu.itis.gimaletdinova.quizapp.data.ExceptionHandlerDelegate
import ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums.LevelDifficulty
import ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums.QuestionType
import ru.kpfu.itis.gimaletdinova.quizapp.data.runCatching
import ru.kpfu.itis.gimaletdinova.quizapp.domain.interactor.GetCategoriesUseCase
import ru.kpfu.itis.gimaletdinova.quizapp.domain.interactor.GetTriviaUseCase
import ru.kpfu.itis.gimaletdinova.quizapp.domain.interactor.LevelInteractor
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.CategoriesList
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.QuestionModel
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.QuestionsList
import ru.kpfu.itis.gimaletdinova.quizapp.util.Constants.PLAYERS_QUESTIONS_NUMBER_PROPORTION
import ru.kpfu.itis.gimaletdinova.quizapp.util.Constants.QUESTIONS_NUMBER
import ru.kpfu.itis.gimaletdinova.quizapp.util.Constants.QUESTION_TIME
import ru.kpfu.itis.gimaletdinova.quizapp.util.PrefsKeys
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val getTriviaUseCase: GetTriviaUseCase,
    private val categoriesUseCase: GetCategoriesUseCase,
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate,
    private val prefs: DataStore<Preferences>,
    private val dispatcher: CoroutineDispatcher,
    private val levelInteractor: LevelInteractor
) : ViewModel() {

    private var _isMultiplayer = false
    val isMultiplayer get() = _isMultiplayer

    private val _loadingFlow = MutableStateFlow(false)
    val loadingFlow get() = _loadingFlow.asStateFlow()

    private var questionsList: QuestionsList? = null
    val questionsListSize get() = questionsList?.questions?.size
    private var counter = 0

    private val _questionsFlow = MutableStateFlow<QuestionModel?>(null)
    val questionsFlow: StateFlow<QuestionModel?>
        get() = _questionsFlow

    private var timer: CountDownTimer? = null

    private val _timeFlow = MutableStateFlow(100)
    val timeFlow: StateFlow<Int>
        get() = _timeFlow

    private var _categoriesList: CategoriesList? = null
    val categoriesList get() = _categoriesList

    private val players = mutableListOf<String>()
    private var categoryChoiceCounter = 0
    private var playersIterator: MutableIterator<String>? = null

    var onPause = false

    val scores = HashMap<String, Int>()
    val errorsChannel = Channel<Throwable>()

    fun setMode(isMultiplayer: Boolean) {
        _isMultiplayer = isMultiplayer
    }

    fun getQuestions(categoryId: Int, difficulty: LevelDifficulty = LevelDifficulty.MEDIUM) {

        val amount = if (isMultiplayer) {
            players.size * PLAYERS_QUESTIONS_NUMBER_PROPORTION
        } else {
            QUESTIONS_NUMBER
        }

        viewModelScope.launch {
            _loadingFlow.value = true
            runCatching(exceptionHandlerDelegate) {
                getTriviaUseCase.invoke(
                    amount = amount,
                    categoryId = categoryId,
                    difficulty = difficulty,
                    type = QuestionType.MULTIPLE_CHOICE
                )
            }.onSuccess {
                questionsList = it
            }.onFailure { ex ->
                errorsChannel.send(ex)
            }
            _loadingFlow.value = false
        }
    }

    fun saveLevel(categoryId: Int, levelNumber: Int) {
        if (questionsList != null) {
            viewModelScope.launch {
                levelInteractor.saveQuestions(levelNumber, categoryId, questionsList!!)
            }
        }
    }

    fun updateTimer() {
        timer?.cancel()

        if (counter >= questionsListSize!!) {
            counter = 0
            _timeFlow.value = -1
            return
        }
        _questionsFlow.value = questionsList?.questions?.get(counter)
        counter++

        _timeFlow.value = 100
        timer = object : CountDownTimer(QUESTION_TIME, 100) {

            override fun onTick(millisUntilFinished: Long) {
                _timeFlow.value = (millisUntilFinished * 100).div(QUESTION_TIME).toInt()
            }

            override fun onFinish() {}
        }.start()
    }

    suspend fun setPlayers(names: List<String>?) {
        if (names == null) {
            val username = viewModelScope.async {
                withContext(dispatcher) {
                    prefs.data.firstOrNull()?.get(PrefsKeys.USERNAME_KEY) ?: "user"
                }
            }.await()
            players.add(username)
        } else {
            players.addAll(names)
        }
        for (player in players) {
            scores[player] = 0
        }
        playersIterator = players.iterator()
    }

    fun getPlayer(): String {
        if (playersIterator != null) {
            if (playersIterator!!.hasNext().not()) playersIterator = players.iterator()
            return playersIterator!!.next()
        }
        return ""
    }

    fun getPlayerToCategoryChoice(): String {
        return players[categoryChoiceCounter++]
    }

    fun getCategoriesList() {
        viewModelScope.launch {
            _loadingFlow.value = true
            runCatching(exceptionHandlerDelegate) {
                categoriesUseCase.invoke()
            }.onSuccess {
                _categoriesList = it
            }.onFailure { ex ->
                errorsChannel.send(ex)
            }
            _loadingFlow.value = false
        }
    }

    fun saveScores(player: String) {
        val score = scores[player] ?: 0
        scores[player] = score + 1
    }

    fun saveUserAnswer(position: Int) {
        questionsList?.questions?.get(counter - 1)?.userAnswerPosition = position
    }

    fun isGameOver(): Boolean {
        if (isMultiplayer.not()) {
            return true
        }
        return categoryChoiceCounter == players.size
    }

    fun clear() {
        timer?.cancel()
        scores.clear()
        categoryChoiceCounter = 0
        players.clear()
        onPause = false
        counter = 0
    }

    override fun onCleared() {
        errorsChannel.close()
        super.onCleared()
    }

}
