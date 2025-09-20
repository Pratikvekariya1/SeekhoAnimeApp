package com.seekho.animeapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GenreDto(
    @SerializedName("mal_id") val malId: Int,
    @SerializedName("name") val name: String
)
