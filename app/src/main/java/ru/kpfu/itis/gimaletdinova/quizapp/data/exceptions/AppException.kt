package ru.kpfu.itis.gimaletdinova.quizapp.data.exceptions

sealed class AppException(message: String) : Exception(message) {

    class SuchEmailAlreadyRegistered(message: String) : AppException(message)
    class InvalidPassword(message: String) : AppException(message)
    class EmailNotFound(message: String) : AppException(message)

    class UserNotFoundException(message: String) : AppException(message)

    class EmptyQuestionsListException(message: String) : AppException(message)
    class EmptyCategoriesListException(message: String) : AppException(message)

    class UploadDataFailed(message: String) : AppException(message)
    class Unknown(message: String) : AppException(message)
}