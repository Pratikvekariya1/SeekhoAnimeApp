package com.seekho.animeapp.domain.repository

import androidx.lifecycle.LiveData
import com.seekho.animeapp.domain.model.Anime
import com.seekho.animeapp.utils.Resource

interface AnimeRepository {
    fun getAllAnime(): LiveData<List<Anime>>
    suspend fun refreshAnimeList(): Resource<Unit>
    suspend fun getAnimeDetails(animeId: Int): Resource<Anime>
    suspend fun searchAnime(query: String): Resource<List<Anime>>
    suspend fun toggleFavorite(animeId: Int): Resource<Unit>
    fun getFavoriteAnime(): LiveData<List<Anime>>
}
