package com.seekho.animeapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TrailerDto(
    @SerializedName("youtube_id") val youtubeId: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("embed_url") val embedUrl: String?
)
