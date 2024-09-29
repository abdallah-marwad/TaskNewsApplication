package com.androiddevs.mvvmnewsapp.data.db

import androidx.room.TypeConverter
import com.androiddevs.mvvmnewsapp.data.models.article.Source

class Converters {
    @TypeConverter
    fun fromSource(source: Source) = source.name
    @TypeConverter
    fun toSource(name: String) = Source(name,name)

    @TypeConverter
    fun fromIntArray(intArray: List<Int>?): String? {
        return intArray?.joinToString(",")
    }

    @TypeConverter
    fun toIntArray(data: String?): List<Int>? {
        return data?.split(",")?.map { it.toInt() }
    }
}