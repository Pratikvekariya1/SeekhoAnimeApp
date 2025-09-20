package com.seekho.animeapp.data.local.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.seekho.animeapp.data.local.database.entity.AnimeEntity

@Dao
interface AnimeDao {
    
    @Query("SELECT * FROM anime_table ORDER BY score DESC")
    fun getAllAnime(): LiveData<List<AnimeEntity>>
    
    @Query("SELECT * FROM anime_table WHERE malId = :id")
    suspend fun getAnimeById(id: Int): AnimeEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnime(anime: AnimeEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAnime(animeList: List<AnimeEntity>)
    
    @Query("DELETE FROM anime_table")
    suspend fun clearAll()
    
    @Query("UPDATE anime_table SET isFavorite = :isFavorite WHERE malId = :id")
    suspend fun updateFavoriteStatus(id: Int, isFavorite: Boolean)
    
    @Query("SELECT * FROM anime_table WHERE isFavorite = 1")
    fun getFavoriteAnime(): LiveData<List<AnimeEntity>>
}
