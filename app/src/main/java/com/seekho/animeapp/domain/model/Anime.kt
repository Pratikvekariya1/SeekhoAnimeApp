package com.seekho.animeapp.domain.model

data class Anime(
    val malId: Int,
    val title: String,
    val imageUrl: String?,
    val score: Double?,
    val episodes: Int?,
    val status: String?,
    val synopsis: String?,
    val trailerUrl: String?,
    val genres: List<String>,
    val isFavorite: Boolean = false
)
