package com.seekho.animeapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AnimeItemDto(
    @SerializedName("mal_id") val malId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("images") val images: ImagesDto,
    @SerializedName("score") val score: Double?,
    @SerializedName("episodes") val episodes: Int?,
    @SerializedName("status") val status: String?,
    @SerializedName("synopsis") val synopsis: String?,
    @SerializedName("genres") val genres: List<GenreDto>?,
    @SerializedName("trailer") val trailer: TrailerDto?
)
