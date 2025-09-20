package com.seekho.animeapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AnimeListResponseDto(
    @SerializedName("data") val data: List<AnimeItemDto>,
    @SerializedName("pagination") val pagination: PaginationDto?
)

data class PaginationDto(
    @SerializedName("last_visible_page") val lastVisiblePage: Int?,
    @SerializedName("has_next_page") val hasNextPage: Boolean?
)
