package com.komorebi.reader.data.local.converters

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.DateTimeFormatter
import kotlinx.datetime.format.jvm
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.UUID

class Converters {
    private val json = Json { ignoreUnknownKeys = true }
    private val formatter = DateTimeFormatter.ISO_DATE_TIME

    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): String? = value?.format(formatter)

    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? = value?.let { LocalDateTime.parse(it, formatter) }

    @TypeConverter
    fun fromStringList(list: List<String>): String = json.encodeToString(list)

    @TypeConverter
    fun toStringList(json: String): List<String> = json.decodeFromString()

    @TypeConverter
    fun fromIntList(list: List<Int>): String = json.encodeToString(list)

    @TypeConverter
    fun toIntList(json: String): List<Int> = json.decodeFromString()

    @TypeConverter
    fun fromLongList(list: List<Long>): String = json.encodeToString(list)

    @TypeConverter
    fun toLongList(json: String): List<Long> = json.decodeFromString()

    @TypeConverter
    fun fromBookType(type: com.komorebi.reader.data.local.entity.BookType): String = type.name

    @TypeConverter
    fun toBookType(name: String): com.komorebi.reader.data.local.entity.BookType =
        com.komorebi.reader.data.local.entity.BookType.valueOf(name)

    @TypeConverter
    fun fromBookStatus(status: com.komorebi.reader.data.local.entity.BookStatus): String = status.name

    @TypeConverter
    fun toBookStatus(name: String): com.komorebi.reader.data.local.entity.BookStatus =
        com.komorebi.reader.data.local.entity.BookStatus.valueOf(name)

    @TypeConverter
    fun fromScanStatus(status: com.komorebi.reader.data.local.entity.ScanStatus): String = status.name

    @TypeConverter
    fun toScanStatus(name: String): com.komorebi.reader.data.local.entity.ScanStatus =
        com.komorebi.reader.data.local.entity.ScanStatus.valueOf(name)

    @TypeConverter
    fun fromMap(map: Map<String, String>): String = json.encodeToString(map)

    @TypeConverter
    fun toMap(json: String): Map<String, String> = json.decodeFromString()

    @TypeConverter
    fun fromAny(obj: Any): String = json.encodeToString(obj)

    @TypeConverter
    fun <T> toAny(json: String, clazz: KClass<T>): T = json.decodeFromString(clazz.createType())
}