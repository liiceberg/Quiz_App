package ru.kpfu.itis.gimaletdinova.quizapp.data.exceptions


open class HttpException(message: String?) : Exception(message) {

    class BadRequestException(message: String) : HttpException(message)
    class UnauthorizedException(message: String) : HttpException(message)
    class NotFoundException(message: String) : HttpException(message)
    class ConflictException(message: String) : HttpException(message)
    class TooManyRequestsException(message: String) : HttpException(message)
    class ServerException(message: String) : HttpException(message)

    object StatusCode {
        const val BAD_REQUEST = 400
        const val UNAUTHORIZED = 401
        const val NOT_FOUND = 404
        const val CONFLICT = 409
        const val TOO_MANY_REQUESTS = 429

        val ServerErrorCodes = 500..599
    }
}