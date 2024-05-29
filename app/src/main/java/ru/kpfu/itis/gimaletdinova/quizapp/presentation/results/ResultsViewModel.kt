package ru.kpfu.itis.gimaletdinova.quizapp.presentation.results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.response.Score
import ru.kpfu.itis.gimaletdinova.quizapp.domain.interactor.RoomInteractor
import ru.kpfu.itis.gimaletdinova.quizapp.domain.interactor.ScoreInteractor
import javax.inject.Inject

@HiltViewModel
class ResultsViewModel @Inject constructor(
    private val roomInteractor: RoomInteractor,
    private val scoreInteractor: ScoreInteractor
) : ViewModel() {

    private val _resultsFlow = MutableStateFlow<List<Score>?>(null)
    val resultsFlow get() = _resultsFlow
    val errorsChannel = Channel<Throwable>()

    fun saveScores(correctAnswersNumber: Int, answersNumber: Int) {
        viewModelScope.launch {
            scoreInteractor.saveScores(answersNumber, correctAnswersNumber)
        }
    }

    fun getResults(room: String) {
        viewModelScope.launch {
            runCatching {
                roomInteractor.getResults(room)
            }.onSuccess {
                _resultsFlow.value = it
            }
        }
    }

    override fun onCleared() {
        errorsChannel.close()
    }

}