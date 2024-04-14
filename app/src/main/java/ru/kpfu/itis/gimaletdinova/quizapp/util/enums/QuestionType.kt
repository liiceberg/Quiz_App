package ru.kpfu.itis.gimaletdinova.quizapp.util.enums


enum class QuestionType {

    MULTIPLE_CHOICE;

    override fun toString(): String {
        return when (this) {
            MULTIPLE_CHOICE -> "multiple"
        }
    }
}