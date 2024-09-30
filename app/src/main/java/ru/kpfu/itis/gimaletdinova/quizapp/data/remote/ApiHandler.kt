package ru.kpfu.itis.gimaletdinova.quizapp.data.remote

import com.google.gson.Gson
import retrofit2.Response
import ru.kpfu.itis.gimaletdinova.quizapp.data.exceptions.ConnectionException
import ru.kpfu.itis.gimaletdinova.quizapp.data.exceptions.HttpException
import ru.kpfu.itis.gimaletdinova.quizapp.data.exceptions.HttpException.StatusCode
import ru.kpfu.itis.gimaletdinova.quizapp.data.remote.pojo.request.HttpError
import java.io.IOException

private val gson = Gson()

suspend fun <T> makeSafeApiCall(apiCall: suspend () -> Response<T>): T {
    return makeApiCall { apiCall() }.parseResult()
}

private suspend fun <T> makeApiCall(apiCall: suspend () -> Response<T>): ResultWrapper<T> {
    return try {
        val response = apiCall()
        if (response.isSuccessful) {
            response.body()?.let {
                ResultWrapper.Success(it)
            } ?: ResultWrapper.GenericError(response.code())
        } else {
            val code = response.code()
            var message: String? = null
            response.errorBody()?.let {
                message = gson.fromJson(it.string(), HttpError::class.java).message
            }
            ResultWrapper.GenericError(code, message)
        }
    } catch (ex: Exception) {
        when (ex) {
            is IOException -> ResultWrapper.NetworkError
            is retrofit2.HttpException -> {
                val code = ex.code()
                val errorResponse = ex.response()?.errorBody()?.string()
                ResultWrapper.GenericError(code, errorResponse)
            }

            else -> ResultWrapper.GenericError(null, ex.localizedMessage)
        }
    }
}

private fun <T> ResultWrapper<T>.parseResult(): T {
    return when (this) {

        is ResultWrapper.Success -> this.value

        is ResultWrapper.GenericError -> {
            when (code) {
                StatusCode.BAD_REQUEST -> {
                    throw HttpException.BadRequestException(error ?: "Request is incorrect")
                }

                StatusCode.UNAUTHORIZED -> {
                    throw HttpException.UnauthorizedException(error ?: "Invalid credentials")
                }

                StatusCode.NOT_FOUND -> {
                    throw HttpException.NotFoundException(error ?: "Can't find information")
                }

                StatusCode.CONFLICT -> {
                    throw HttpException.ConflictException(error ?: "Such item already exist")
                }

                StatusCode.TOO_MANY_REQUESTS -> {
                    throw HttpException.TooManyRequestsException(error ?: "The number of request attempts exceeded")
                }

                in StatusCode.ServerErrorCodes -> {
                    throw HttpException.ServerException(error ?: "Server is not response")
                }

                else -> throw HttpException(error)
            }
        }

        is ResultWrapper.NetworkError -> throw ConnectionException("Can't connect to the server")
    }
}
