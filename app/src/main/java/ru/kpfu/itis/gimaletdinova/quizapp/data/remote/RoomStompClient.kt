package ru.kpfu.itis.gimaletdinova.quizapp.data.remote

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.kpfu.itis.gimaletdinova.quizapp.BuildConfig
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.request.Message
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.request.Message.Code
import ru.kpfu.itis.gimaletdinova.quizapp.util.NetworkConstants.STOMP_CONNECTION_PATH
import ru.kpfu.itis.gimaletdinova.quizapp.util.NetworkConstants.STOMP_SEND_PATH
import ru.kpfu.itis.gimaletdinova.quizapp.util.NetworkConstants.STOMP_SUBSCRIBE_PATH
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompMessage
import javax.inject.Inject

class RoomStompClient @Inject constructor() {

    private val stompClient = Stomp.over(
    Stomp.ConnectionProvider.OKHTTP,
    BuildConfig.BASE_URL + STOMP_CONNECTION_PATH
    )

    private lateinit var compositeDisposable: CompositeDisposable
    private lateinit var subscribeRoom: String

    private var onReceive: (topicMessage: StompMessage) -> Unit = {}
    private var onSendMessage: (message: Message) -> Unit = {}
    private var onSendMessageError: (error: Throwable) -> Unit = {}

    fun init(
        onReceive: (topicMessage: StompMessage) -> Unit,
        onSendMessage: (message: Message) -> Unit,
        onSendMessageError: (error: Throwable) -> Unit,
        room: String,
        userId: Long
    ) {
        this.onReceive = onReceive
        this.onSendMessage = onSendMessage
        this.onSendMessageError = onSendMessageError

        compositeDisposable = CompositeDisposable()
        subscribeRoom = room
        subscribe()
        connect(userId)
    }


    fun sendMessage(message: Message) {
        if (this::subscribeRoom.isInitialized) {
            compositeDisposable.add(
                stompClient.send(
                    STOMP_SEND_PATH + subscribeRoom, Message.toJson(message)
                ).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            Log.i("TAG", "Stomp send")
                            onSendMessage(message)
                        },
                        {
                            Log.e("TAG", "Stomp error", it)
                            onSendMessageError(it)
                        }
                    )
            )
        }
    }

    private fun subscribe() {
        val topicSubscribe = stompClient.topic(STOMP_SUBSCRIBE_PATH + subscribeRoom)
            .subscribeOn(Schedulers.io(), false)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { topicMessage ->
                    onReceive(topicMessage)
                },
                {
                    Log.e("ERROR TAG", "Topic subscribe error", it)
                }
            )

        val lifecycleSubscribe = stompClient.lifecycle()
            .subscribeOn(Schedulers.io(), false)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { lifecycleEvent: LifecycleEvent ->
                when (lifecycleEvent.type!!) {
                    LifecycleEvent.Type.OPENED -> {
                        Log.d("TAG", "Stomp connection opened")
                    }

                    LifecycleEvent.Type.ERROR -> {
                        Log.e("TAG", "Lifecycle subscribe error", lifecycleEvent.exception)
                    }

                    LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT, LifecycleEvent.Type.CLOSED -> {
                        Log.d("TAG", "Stomp connection closed")
                    }
                }
            }

        compositeDisposable.add(lifecycleSubscribe)
        compositeDisposable.add(topicSubscribe)
    }

    private fun connect(userId: Long) {
        if (stompClient.isConnected.not()) {
            stompClient.connect()
            sendMessage(
                Message(sender = userId, code = Code.JOIN)
            )
        }
    }

    fun close() {
        stompClient.disconnect()
        if (this::compositeDisposable.isInitialized) {
            compositeDisposable.dispose()
        }
    }
}