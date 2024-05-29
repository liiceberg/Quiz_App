package ru.kpfu.itis.gimaletdinova.quizapp.data

import android.content.Context
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.HttpException
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.data.exceptions.BadRequestException
import ru.kpfu.itis.gimaletdinova.quizapp.data.exceptions.ConnectionException
import ru.kpfu.itis.gimaletdinova.quizapp.data.exceptions.PageNotFoundException
import ru.kpfu.itis.gimaletdinova.quizapp.data.exceptions.ServerException
import ru.kpfu.itis.gimaletdinova.quizapp.data.exceptions.TooManyRequestsException
import ru.kpfu.itis.gimaletdinova.quizapp.data.exceptions.UnauthorizedException
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.request.HttpError
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject

class ExceptionHandlerDelegate @Inject constructor(
    @ApplicationContext private val ctx: Context
) {
    private val gson = Gson()
    fun handleException(ex: Throwable): Throwable {
        println(ex.javaClass)
        return when (ex) {
            is HttpException -> {
                var message: String? = null
                ex.response()?.errorBody()?.let {
                    message = gson.fromJson(it.string(), HttpError::class.java).message
                }
                when (ex.code()) {
                    400 -> BadRequestException(message ?: ctx.getString(R.string.bad_request))
                    401 -> UnauthorizedException(message ?: ctx.getString(R.string.unauthorized))
                    404 -> PageNotFoundException(message ?: ctx.getString(R.string.page_not_found))
                    429 -> TooManyRequestsException(
                        message ?: ctx.getString(R.string.too_many_requests)
                    )

                    in 500..599 -> ServerException(
                        message ?: ctx.getString(R.string.server_exception)
                    )

                    else -> ex
                }
            }

            is SocketTimeoutException, is ConnectException -> ConnectionException(ctx.getString(R.string.connection_exception))
            else -> ex
        }
    }
}