package ru.kpfu.itis.gimaletdinova.quizapp.presentation.game

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.gimaletdinova.quizapp.data.ExceptionHandlerDelegate
import ru.kpfu.itis.gimaletdinova.quizapp.data.runCatching
import ru.kpfu.itis.gimaletdinova.quizapp.domain.model.QuestionModel
import ru.kpfu.itis.gimaletdinova.quizapp.domain.usecase.GetTriviaUseCase
import ru.kpfu.itis.gimaletdinova.quizapp.util.Constants.QUESTIONS_NUMBER
import ru.kpfu.itis.gimaletdinova.quizapp.util.Constants.QUESTION_TIME
import ru.kpfu.itis.gimaletdinova.quizapp.util.enums.LevelDifficulty
import ru.kpfu.itis.gimaletdinova.quizapp.util.enums.QuestionType
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val getTriviaUseCase: GetTriviaUseCase,
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate
) : ViewModel() {
    private val _loadingFlow = MutableStateFlow(false)
    val loadingFlow get() = _loadingFlow.asStateFlow()

    private var questionsList: List<QuestionModel>? = null
    private var counter = 0

    private val _questionsFlow = MutableStateFlow<QuestionModel?>(null)
    val questionsFlow: StateFlow<QuestionModel?>
        get() = _questionsFlow

    private var timer: CountDownTimer? = null

    private val _timeFlow = MutableStateFlow(100)
    val timeFlow: StateFlow<Int>
        get() = _timeFlow

    fun getQuestions(categoryId: Int, difficulty: LevelDifficulty = LevelDifficulty.MEDIUM) {
        viewModelScope.launch {
            _loadingFlow.value = true
            runCatching(exceptionHandlerDelegate) {
            getTriviaUseCase.invoke(
                    amount = QUESTIONS_NUMBER,
                    categoryId = categoryId,
                    difficulty = difficulty,
                    type = QuestionType.MULTIPLE_CHOICE
                )
            }.onSuccess { data ->
                questionsList = data.questions
            }
            _loadingFlow.value = false
        }
    }

    fun updateTimer() {
        timer?.cancel()

        if (counter >= questionsList!!.size) {
            counter = 0
            _timeFlow.value = -1
            return
        }

        _questionsFlow.value = questionsList?.get(counter)
        counter++

        _timeFlow.value = 100
        timer = object : CountDownTimer(QUESTION_TIME, 100) {

            override fun onTick(millisUntilFinished: Long) {
                _timeFlow.value = (millisUntilFinished * 100).div(QUESTION_TIME).toInt()
            }

            override fun onFinish() {
                if (counter < QUESTIONS_NUMBER) updateTimer()
            }
        }.start()
    }
}
