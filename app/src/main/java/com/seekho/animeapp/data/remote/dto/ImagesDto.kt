package com.seekho.animeapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ImagesDto(
    @SerializedName("jpg") val jpg: ImageSetDto,
    @SerializedName("webp") val webp: ImageSetDto?
)

data class ImageSetDto(
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("small_image_url") val smallImageUrl: String?,
    @SerializedName("large_image_url") val largeImageUrl: String?
)
