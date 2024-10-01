package ru.kpfu.itis.gimaletdinova.quizapp.presentation.multiplayer.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.gimaletdinova.quizapp.data.ExceptionHandlerDelegate
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.RoomStompClient
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.request.Message
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.request.Message.Code
import ru.kpfu.itis.gimaletdinova.quizapp.data.runCatching
import ru.kpfu.itis.gimaletdinova.quizapp.domain.interactor.RoomInteractor
import ru.kpfu.itis.gimaletdinova.quizapp.domain.interactor.UserInteractor
import ru.kpfu.itis.gimaletdinova.quizapp.util.WorkRepeater
import ua.naiksoftware.stomp.dto.StompMessage
import javax.inject.Inject


@HiltViewModel
class RoomViewModel @Inject constructor(
    private val userInteractor: UserInteractor,
    private val roomInteractor: RoomInteractor,
    private val exceptionHandlerDelegate: ExceptionHandlerDelegate,
    private val stompClient: RoomStompClient,
    private val workRepeater: WorkRepeater
) : ViewModel() {

    private var _initialized = false
    val initialized get() = _initialized

    private var userId: Long = -1

    private val _messages = mutableListOf<String>()
    val messages get() = _messages.toList()

    private val _messageFlow = MutableSharedFlow<String>()
    val messageFlow get() = _messageFlow.asSharedFlow()
    
    private val _unreadyPlayersNumberFlow = MutableStateFlow(UNREADY_PLAYERS_INIT_VALUE)
    val unreadyPlayersNumberFlow get() = _unreadyPlayersNumberFlow.asStateFlow()

    private val _notFinishedPlayersNumberFlow = MutableStateFlow(NOT_FINISHED_PLAYERS_INIT_VALUE)
    val notFinishedPlayersNumberFlow get() = _notFinishedPlayersNumberFlow.asStateFlow()
    
    private val _remainingCapacityFlow = MutableStateFlow(REMAINING_CAPACITY_INIT_VALUE)
    val remainingCapacityFlow get() = _remainingCapacityFlow.asStateFlow()
    
    private val _exitFlow = MutableStateFlow(false)
    val exitFlow get() = _exitFlow.asStateFlow()
    
    private var _room: String? = null
    val room get() = _room ?: ""

    private var _players: List<String>? = null
    val players get() = _players

    val errorsChannel = Channel<Throwable>()

    fun initStomp(room: String) {
        _room = room
        viewModelScope.launch {
            userId = userInteractor.getUserId()
            stompClient.init(
                ::onReceiveMessage,
                ::onSendMessage,
                ::onSendMessageError,
                room,
                userId
            )
            workRepeater.doRepeatWork(ALIVE_MESSAGE_SEND_INTERVAL, ::sendAliveMessage)
        }
        _initialized = true
    }


    fun exit() {
        stompClient.sendMessage(Message(sender = userId, code = Code.EXIT))
    }

    fun getPlayers() {
        viewModelScope.launch {
            runCatching(exceptionHandlerDelegate) {
                roomInteractor.getPlayers(room)
            }.onSuccess {
                _players = it
            }.onFailure {
                errorsChannel.send(it)
            }
        }
    }

    private fun sendAliveMessage() {
        stompClient.sendMessage(Message(userId, Code.ALIVE))
    }

    fun sendScores(scores: Int) {
        stompClient.sendMessage(
            Message(sender = userId, code = Code.SCORE, score = scores)
        )
    }

    fun sendReadyMessage() {
        stompClient.sendMessage(Message(userId, Code.READY))
    }

    fun resetUnreadyPlayersNumber() {
        _unreadyPlayersNumberFlow.value = UNREADY_PLAYERS_INIT_VALUE
    }

    fun resetNotFinishedPlayersNumber() {
        _notFinishedPlayersNumberFlow.value = NOT_FINISHED_PLAYERS_INIT_VALUE
    }

    private fun resetJoinFlow() {
        _remainingCapacityFlow.value = REMAINING_CAPACITY_INIT_VALUE
    }

    private fun onReceiveMessage(topicMessage: StompMessage) {
        Message.fromJson(topicMessage.payload).run {
            message?.let {
                viewModelScope.launch {
                    _messageFlow.emit(message)
                    _messages.add(message)
                }
            }
            wait?.let {
                when (code) {
                    Code.READY -> _unreadyPlayersNumberFlow.value = it
                    Code.SCORE -> _notFinishedPlayersNumberFlow.value = it
                    Code.JOIN -> _remainingCapacityFlow.value = it
                    else -> {}
                }
            }
        }
    }

    private fun onSendMessage(message: Message) {
        if (message.code == Code.EXIT) {
            _exitFlow.value = true
        }
    }

    private fun onSendMessageError(error: Throwable) {
        viewModelScope.launch {
            errorsChannel.send(error)
        }
    }

    fun clear() {
        workRepeater.stopRepeatWork()
        stompClient.close()
        _initialized = false
        _messages.clear()
        resetNotFinishedPlayersNumber()
        resetUnreadyPlayersNumber()
        resetJoinFlow()
        _exitFlow.value = false
    }

    override fun onCleared() {
        workRepeater.cancel()
        errorsChannel.close()
        stompClient.close()
        clear()
    }

    companion object {
        private const val ALIVE_MESSAGE_SEND_INTERVAL = 10_000L

        private const val UNREADY_PLAYERS_INIT_VALUE = -1
        private const val NOT_FINISHED_PLAYERS_INIT_VALUE = -1
        private const val REMAINING_CAPACITY_INIT_VALUE = 0
    }

}