package com.seekho.animeapp.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.seekho.animeapp.data.local.database.Converters

@Entity(tableName = "anime_table")
@TypeConverters(Converters::class)
data class AnimeEntity(
    @PrimaryKey val malId: Int,
    val title: String,
    val imageUrl: String?,
    val score: Double?,
    val episodes: Int?,
    val status: String?,
    val synopsis: String?,
    val trailerUrl: String?,
    val genres: List<String>,
    val isFavorite: Boolean = false,
    val lastUpdated: Long = System.currentTimeMillis()
)
