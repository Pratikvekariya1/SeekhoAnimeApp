package com.seekho.animeapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AnimeDetailResponseDto(
    @SerializedName("data") val data: AnimeItemDto
)
