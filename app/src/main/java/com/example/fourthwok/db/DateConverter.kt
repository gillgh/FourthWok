package com.example.fourthwok.db

import androidx.room.TypeConverter
import java.util.*

class DateConverter {

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toDate(value: Long?): Date? {
        return value?.let { Date(it) }
    }
}