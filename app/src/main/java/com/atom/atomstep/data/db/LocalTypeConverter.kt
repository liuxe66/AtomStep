package com.atom.atomstep.data.db

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * <pre>
 *     author: dhl
 *     date  : 2020/7/21
 *     desc  :
 * </pre>
 */
open class LocalTypeConverter {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    @TypeConverter
    open fun fromLocalDate(dateTime: LocalDate?): String? {
        return dateTime?.format(formatter)
    }

    @TypeConverter
    open fun toLocalDate(dateTimeString: String?): LocalDate? {
        return if (dateTimeString != null) LocalDate.parse(dateTimeString, formatter) else null
    }
}