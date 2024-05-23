package ru.kpfu.itis.gimaletdinova.quizapp.presentation.results

import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.response.Score
import ru.kpfu.itis.gimaletdinova.quizapp.domain.interactor.RoomInteractor
import ru.kpfu.itis.gimaletdinova.quizapp.util.PrefsKeys
import javax.inject.Inject

@HiltViewModel
class ResultsViewModel @Inject constructor(
    private val prefs: DataStore<Preferences>,
    private val dispatcher: CoroutineDispatcher,
    private val roomInteractor: RoomInteractor
) : ViewModel() {

    private val _resultsFlow = MutableStateFlow<List<Score>?>(null)
    val resultsFlow get() = _resultsFlow

    fun saveScores(correctAnswersNumber: Int, answersNumber: Int) {
        viewModelScope.launch {
            withContext(dispatcher) {
                prefs.edit {
                    val totalScores = it[PrefsKeys.TOTAL_QUESTIONS_KEY] ?: 0
                    it[PrefsKeys.TOTAL_QUESTIONS_KEY] = totalScores + answersNumber

                    val userScores = it[PrefsKeys.USER_QUESTIONS_KEY] ?: 0
                    it[PrefsKeys.USER_QUESTIONS_KEY] = userScores + correctAnswersNumber
                }
            }
        }
    }

    suspend fun getResults(room: String) {
        runCatching {
            roomInteractor.getResults(room)
        }.onSuccess {
            _resultsFlow.value = it
        }

    }

}