package edu.card.clarity.data.converters

import androidx.room.TypeConverter
import java.util.Date

class DateConverter {
    @TypeConverter
    fun toDate(l: Long?): Date? = l?.let { Date(it) }

    @TypeConverter
    fun fromDate(d: Date?): Long? = d?.time
}