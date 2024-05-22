package ru.kpfu.itis.gimaletdinova.quizapp.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.HttpException
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.data.exceptions.BadRequestException
import ru.kpfu.itis.gimaletdinova.quizapp.data.exceptions.PageNotFoundException
import ru.kpfu.itis.gimaletdinova.quizapp.data.exceptions.ServerException
import ru.kpfu.itis.gimaletdinova.quizapp.data.exceptions.TooManyRequestsException
import javax.inject.Inject

class ExceptionHandlerDelegate @Inject constructor(
    @ApplicationContext private val ctx: Context
) {
    fun handleException(ex: Throwable): Throwable {
        println(ex.javaClass)
        return when (ex) {
            is HttpException -> {
                when (ex.code()) {
                    400 -> {
                        println(ex.response()?.errorBody()?.string())
                        BadRequestException(message = ctx.getString(R.string.bad_request))
                    }
                    404 -> PageNotFoundException(message = ctx.getString(R.string.page_not_found))
                    429 -> TooManyRequestsException(message = ctx.getString(R.string.too_many_requests))
                    in 500..599 -> ServerException(message = ctx.getString(R.string.server_exception))
                    else -> ex
                }
            }
            else -> ex
        }
    }
}