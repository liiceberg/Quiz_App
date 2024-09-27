package ru.kpfu.itis.gimaletdinova.quizapp.data

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.kpfu.itis.gimaletdinova.quizapp.R
import ru.kpfu.itis.gimaletdinova.quizapp.data.exceptions.AppException
import ru.kpfu.itis.gimaletdinova.quizapp.data.exceptions.ConnectionException
import ru.kpfu.itis.gimaletdinova.quizapp.data.exceptions.HttpException
import javax.inject.Inject

class ExceptionHandlerDelegate @Inject constructor(
    @ApplicationContext private val ctx: Context
) {
    fun handleException(ex: Exception): Exception {
        Log.d("EXCEPTION", ex.toString())
        return when (ex) {
            is AppException -> {
                ex
            }
            is HttpException.UnauthorizedException -> {
                AppException.UploadDataFailed(ctx.getString(R.string.unauthorized_error))
            }
            is ConnectionException -> {
                AppException.UploadDataFailed(ctx.getString(R.string.data_loading_error))
            }
            else -> {
                AppException.Unknown(ctx.getString(R.string.unknown_error))
            }
        }
    }
}