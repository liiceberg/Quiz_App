package ru.kpfu.itis.gimaletdinova.quizapp.presentation.game

import android.content.Context
import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.data.ExceptionHandlerDelegate
import ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums.LevelDifficulty
import ru.kpfu.itis.gimaletdinova.quizapp.data.model.enums.QuestionType
import ru.kpfu.itis.gimaletdinova.quizapp.data.runCatching
import ru.kpfu.itis.gimaletdinova.quizapp.domain.interactor.GetCategoriesUseCase
import ru.kpfu.itis.gimaletdinova.quizapp.domain.interactor.GetTriviaUseCase
import ru.kpfu.itis.gimaletdinova.quizapp.domain.interactor.LevelInteractor
import ru.kpfu.itis.gimaletdinova.quizapp.domain.interactor.RoomInteractor
import ru.kpfu.itis.gimaletdinova.quizapp.domain.interactor.UserInteractor
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.CategoriesList
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.QuestionModel
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.QuestionsList
import ru.kpfu.itis.gimaletdinova.quizapp.util.GameConfigConstants.PLAYERS_QUESTIONS_NUMBER_PROPORTION
import ru.kpfu.itis.gimaletdinova.quizapp.util.GameConfigConstants.QUESTIONS_NUMBER
import ru.kpfu.itis.gimaletdinova.quizapp.util.GameConfigConstants.QUESTION_TIME
import ru.kpfu.itis.gimaletdinova.quizapp.util.Mode
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val getTriviaUseCase: GetTriviaUseCase,
    private val categoriesUseCase: GetCategoriesUseCase,
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate,
    private val userInteractor: UserInteractor,
    private val levelInteractor: LevelInteractor,
    private val roomInteractor: RoomInteractor,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private var _mode = Mode.SINGLE
    val mode get() = _mode

    private val _loadingFlow = MutableStateFlow(false)
    val loadingFlow get() = _loadingFlow.asStateFlow()

    private var questionsList: QuestionsList? = null
    val questionsListSize get() = questionsList?.questions?.size
    private var counter = 0

    private val _questionsFlow = MutableStateFlow<QuestionModel?>(null)
    val questionsFlow: StateFlow<QuestionModel?>
        get() = _questionsFlow

    private var timer: CountDownTimer? = null

    private val _timeFlow = MutableStateFlow(INIT_TIMER_VALUE)
    val timeFlow: StateFlow<Int> get() = _timeFlow

    private var _categoriesList: CategoriesList? = null
    val categoriesList get() = _categoriesList

    private val players = mutableListOf<String>()
    private var categoryChoiceCounter = 0
    private var playersIterator: MutableIterator<String>? = null

    var onPause = false

    val scores = HashMap<String, Int>()
    val errorsChannel = Channel<Throwable>()

    fun setMode(mode: Mode) {
        _mode = mode
    }

    fun getQuestions(categoryId: Int, difficulty: LevelDifficulty = LevelDifficulty.MEDIUM) {
        val amount = if (mode == Mode.MULTIPLAYER) {
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

    suspend fun getQuestions(room: String) {
        _loadingFlow.value = true
        runCatching(exceptionHandlerDelegate) {
            roomInteractor.getGameContent(room)
        }.onSuccess {
            questionsList = it
        }.onFailure { ex ->
            errorsChannel.send(ex)
        }
        _loadingFlow.value = false

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

        _timeFlow.value = INIT_TIMER_VALUE
        timer = AnswerTimer().start()
    }

    fun setPlayers() {
        setPlayers(null)
    }

    fun setPlayers(names: Array<String>?) {
        viewModelScope.launch {
            if (names == null) {
                var username = context.getString(R.string.default_username)
                runCatching {
                    userInteractor.getUsername()
                }.onSuccess {
                    it?.let {
                        username = it
                    }
                }.onFailure {
                    errorsChannel.send(it)
                }
                players.add(username)
            } else {
                players.addAll(names)
            }

            players.forEach { scores[it] = 0 }
            playersIterator = players.iterator()
        }
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
        scores[player] = scores.getValue(player) + 1
    }

    fun saveUserAnswer(position: Int) {
        questionsList?.questions?.get(counter - 1)?.userAnswerPosition = position
    }

    fun isGameOver(): Boolean {
        if (mode != Mode.MULTIPLAYER) {
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
        playersIterator = null
    }

    override fun onCleared() {
        errorsChannel.close()
    }

    inner class AnswerTimer: CountDownTimer(QUESTION_TIME, COUNT_DOWN_INTERVAL) {

        override fun onTick(millisUntilFinished: Long) {
            _timeFlow.value = (millisUntilFinished * INIT_TIMER_VALUE).div(QUESTION_TIME).toInt()
        }

        override fun onFinish() {}
    }

    companion object {
        private const val COUNT_DOWN_INTERVAL = 100L
        private const val INIT_TIMER_VALUE = 100
    }

}
