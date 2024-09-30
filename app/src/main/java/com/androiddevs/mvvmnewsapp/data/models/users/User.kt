package com.androiddevs.mvvmnewsapp.data.models.users

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.androiddevs.mvvmnewsapp.data.db.Converters

@TypeConverters(Converters::class)
@Entity(tableName = "users" ,
    indices = [Index(value = ["email"], unique = true), Index(value = ["username"], unique = true)])
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0 ,
    val email: String,
    val username: String,
    val password: String,
    val favArticles : List<Int>? = null
)