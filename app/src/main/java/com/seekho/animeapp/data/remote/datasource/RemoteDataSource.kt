package com.seekho.animeapp.data.remote.datasource

import com.seekho.animeapp.data.remote.api.JikanApiService
import com.seekho.animeapp.data.remote.dto.AnimeDetailResponseDto
import com.seekho.animeapp.data.remote.dto.AnimeListResponseDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    private val apiService: JikanApiService
) {
    
    suspend fun getTopAnime(page: Int = 1, limit: Int = 25): AnimeListResponseDto {
        return apiService.getTopAnime(page, limit)
    }
    
    suspend fun getAnimeDetails(animeId: Int): AnimeDetailResponseDto {
        return apiService.getAnimeDetails(animeId)
    }
    
    suspend fun searchAnime(query: String, page: Int = 1, limit: Int = 25): AnimeListResponseDto {
        return apiService.searchAnime(query, page, limit)
    }
}
