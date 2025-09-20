package com.seekho.animeapp.data.local.datasource

import androidx.lifecycle.LiveData
import com.seekho.animeapp.data.local.database.dao.AnimeDao
import com.seekho.animeapp.data.local.database.entity.AnimeEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(
    private val animeDao: AnimeDao
) {
    
    fun getAllAnime(): LiveData<List<AnimeEntity>> {
        return animeDao.getAllAnime()
    }
    
    suspend fun getAnimeById(id: Int): AnimeEntity? {
        return animeDao.getAnimeById(id)
    }
    
    suspend fun insertAnime(anime: AnimeEntity) {
        animeDao.insertAnime(anime)
    }
    
    suspend fun insertAllAnime(animeList: List<AnimeEntity>) {
        animeDao.insertAllAnime(animeList)
    }
    
    suspend fun clearAll() {
        animeDao.clearAll()
    }
    
    suspend fun updateFavoriteStatus(id: Int, isFavorite: Boolean) {
        animeDao.updateFavoriteStatus(id, isFavorite)
    }
    
    fun getFavoriteAnime(): LiveData<List<AnimeEntity>> {
        return animeDao.getFavoriteAnime()
    }
}
