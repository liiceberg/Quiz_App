package ru.kpfu.itis.gimaletdinova.quizapp.presentation.multiplayer

import android.util.Log
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.kpfu.itis.gimaletdinova.quizapp.BuildConfig
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.JwtTokenManager
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.request.Code
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.request.MessageDto
import ru.kpfu.itis.gimaletdinova.quizapp.util.PrefsKeys.USER_ID_KEY
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompMessage
import java.util.Collections
import javax.inject.Inject


@HiltViewModel
class RoomViewModel @Inject constructor(
    private val jwtTokenManager: JwtTokenManager,
    private val prefs: DataStore<Preferences>
) : ViewModel() {

    private var mStompClient: StompClient? = null
    var initialized = false
    private var compositeDisposable: CompositeDisposable? = null
    private val gson = Gson()

    private var _userId: Long? = -1
    val userId get() = _userId

    val messages = mutableListOf<String>()
    val messageFlow = MutableSharedFlow<List<String>>()
    val waitFlow = MutableStateFlow(-1)
    val resultsWaitFlow = MutableStateFlow(-1)
    private var _room: String? = null
    val room get() = _room


    fun initStomp(room: String?) {
        _room = room
        viewModelScope.launch {
            _userId = prefs.data.map { it[USER_ID_KEY] }.first()
            val token = jwtTokenManager.getAccessJwt()
            val headerMap = Collections.singletonMap("Authorization", "Bearer $token")
            mStompClient = Stomp.over(
                Stomp.ConnectionProvider.OKHTTP,
                BuildConfig.BASE_URL + "game-websocket/websocket",
                headerMap
            )

            resetSubscriptions()

            if (mStompClient != null) {
                val topicSubscribe = mStompClient!!.topic("/topic/game/$room")
                    .subscribeOn(Schedulers.io(), false)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ topicMessage: StompMessage ->
                        val message = gson.fromJson(topicMessage.payload, MessageDto::class.java)
                        message.run {
                            this.message?.let {
                                messages.add(it)
                                viewModelScope.launch {
                                    messageFlow.emit(messages)
                                }
                            }
                            wait?.let {
                                when (code) {
                                    Code.READY -> waitFlow.value = it
                                    Code.SCORE -> resultsWaitFlow.value = it
                                    else -> {}
                                }
                            }
                        }
                    },
                        {
                            Log.e("ERROR TAG", "Error!", it)
                        }
                    )

                val lifecycleSubscribe = mStompClient!!.lifecycle()
                    .subscribeOn(Schedulers.io(), false)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { lifecycleEvent: LifecycleEvent ->
                        when (lifecycleEvent.type!!) {
                            LifecycleEvent.Type.OPENED -> Log.d("TAG", "Stomp connection opened")
                            LifecycleEvent.Type.ERROR -> Log.e(
                                "TAG",
                                "Error",
                                lifecycleEvent.exception
                            )

                            LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT,
                            LifecycleEvent.Type.CLOSED -> {
                                Log.d("TAG", "Stomp connection closed")
                            }
                        }
                    }

                compositeDisposable!!.add(lifecycleSubscribe)
                compositeDisposable!!.add(topicSubscribe)

                if (!mStompClient!!.isConnected) {
                    mStompClient!!.connect()
                    sendMessage(
                        MessageDto(sender = _userId, code = Code.JOIN)
                    )
                }
            } else {
                Log.e("TAG", "mStompClient is null!")
            }
            initialized = true
        }
    }

    fun sendMessage(message: MessageDto) {
        if (_room != null) {
            sendCompletable(mStompClient!!.send("/app/game/$_room", gson.toJson(message)))
        }
    }

    private fun sendCompletable(request: Completable) {
        compositeDisposable?.add(
            request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        Log.i("TAG", "Stomp send")
                    },
                    {
                        Log.e("TAG", "Stomp error", it)
                    }
                )
        )
    }

    private fun resetSubscriptions() {
        if (compositeDisposable != null) {
            compositeDisposable!!.dispose()
        }
        compositeDisposable = CompositeDisposable()
    }

    fun clear() {
        if (mStompClient != null) {
            sendMessage(MessageDto(sender = _userId, code = Code.EXIT))
        }
        mStompClient?.disconnect()
        compositeDisposable?.dispose()
        mStompClient = null
        initialized = false
        messages.clear()
    }

    override fun onCleared() {
        clear()
    }

}