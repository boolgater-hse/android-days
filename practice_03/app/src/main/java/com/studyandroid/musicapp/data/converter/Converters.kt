package com.studyandroid.musicapp.data.converter

import androidx.room.TypeConverter
import java.text.DateFormat
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestampToDate(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun fromDateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    companion object {
        val formatter: DateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.UK)
        val calendar: Calendar = Calendar.getInstance()
    }
}
