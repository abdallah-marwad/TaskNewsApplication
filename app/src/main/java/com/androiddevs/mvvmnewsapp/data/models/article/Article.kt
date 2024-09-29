package com.androiddevs.mvvmnewsapp.data.models.article

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(
    tableName = "articles"
)
@Parcelize
class Article(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val author: String?,
    var content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    val title: String?,
    val url: String?,
    val urlToImage: String?
) : Parcelable