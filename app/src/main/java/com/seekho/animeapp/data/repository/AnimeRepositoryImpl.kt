package com.seekho.animeapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.seekho.animeapp.data.local.datasource.LocalDataSource
import com.seekho.animeapp.data.mapper.AnimeMapper
import com.seekho.animeapp.data.remote.datasource.RemoteDataSource
import com.seekho.animeapp.domain.model.Anime
import com.seekho.animeapp.domain.repository.AnimeRepository
import com.seekho.animeapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimeRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : AnimeRepository {

    override fun getAllAnime(): LiveData<List<Anime>> {
        return localDataSource.getAllAnime().map { entities ->
            entities.map { AnimeMapper.mapEntityToDomain(it) }
        }
    }

    override suspend fun refreshAnimeList(): Resource<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = remoteDataSource.getTopAnime()
                val animeEntities = response.data.map { dto ->
                    AnimeMapper.mapDtoToEntity(dto)
                }
                localDataSource.insertAllAnime(animeEntities)
                Resource.Success(Unit)
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    override suspend fun getAnimeDetails(animeId: Int): Resource<Anime> {
        return withContext(Dispatchers.IO) {
            try {
                // First check local database
                val localAnime = localDataSource.getAnimeById(animeId)
                if (localAnime != null) {
                    return@withContext Resource.Success(AnimeMapper.mapEntityToDomain(localAnime))
                }

                // If not found locally, fetch from API
                val response = remoteDataSource.getAnimeDetails(animeId)
                val animeEntity = AnimeMapper.mapDtoToEntity(response.data)
                localDataSource.insertAnime(animeEntity)

                Resource.Success(AnimeMapper.mapEntityToDomain(animeEntity))
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    override suspend fun searchAnime(query: String): Resource<List<Anime>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = remoteDataSource.searchAnime(query)
                val animeList = response.data.map { dto ->
                    AnimeMapper.mapDtoToDomain(dto)
                }
                Resource.Success(animeList)
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    override suspend fun toggleFavorite(animeId: Int): Resource<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val anime = localDataSource.getAnimeById(animeId)
                if (anime != null) {

                    localDataSource.updateFavoriteStatus(animeId, !anime.isFavorite)
                    Resource.Success(!anime.isFavorite)
                } else {
                    Resource.Error("Anime not found")
                }
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    override fun getFavoriteAnime(): LiveData<List<Anime>> {
        return localDataSource.getFavoriteAnime().map { entities ->
            entities.map { AnimeMapper.mapEntityToDomain(it) }
        }
    }
}