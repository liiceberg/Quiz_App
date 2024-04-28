package ru.kpfu.itis.gimaletdinova.quizapp.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class Converter {
    @TypeConverter
    fun listToString(list: List<String>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun stringToList(json: String): List<String?> {
        return Gson().fromJson(json, object : TypeToken<List<String>>() {}.type)
    }
}