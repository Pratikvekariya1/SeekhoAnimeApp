package com.seekho.animeapp.data.mapper

import com.seekho.animeapp.data.local.database.entity.AnimeEntity
import com.seekho.animeapp.data.remote.dto.AnimeItemDto
import com.seekho.animeapp.domain.model.Anime

object AnimeMapper {
    
    fun mapDtoToDomain(dto: AnimeItemDto): Anime {
        return Anime(
            malId = dto.malId,
            title = dto.title,
            imageUrl = dto.images.jpg.largeImageUrl ?: dto.images.jpg.imageUrl,
            score = dto.score,
            episodes = dto.episodes,
            status = dto.status,
            synopsis = dto.synopsis,
            trailerUrl = dto.trailer?.embedUrl,
            genres = dto.genres?.map { it.name } ?: emptyList()
        )
    }
    
    fun mapEntityToDomain(entity: AnimeEntity): Anime {
        return Anime(
            malId = entity.malId,
            title = entity.title,
            imageUrl = entity.imageUrl,
            score = entity.score,
            episodes = entity.episodes,
            status = entity.status,
            synopsis = entity.synopsis,
            trailerUrl = entity.trailerUrl,
            genres = entity.genres,
            isFavorite = entity.isFavorite
        )
    }
    
    fun mapDomainToEntity(domain: Anime): AnimeEntity {
        return AnimeEntity(
            malId = domain.malId,
            title = domain.title,
            imageUrl = domain.imageUrl,
            score = domain.score,
            episodes = domain.episodes,
            status = domain.status,
            synopsis = domain.synopsis,
            trailerUrl = domain.trailerUrl,
            genres = domain.genres,
            isFavorite = domain.isFavorite
        )
    }
    
    fun mapDtoToEntity(dto: AnimeItemDto): AnimeEntity {
        return AnimeEntity(
            malId = dto.malId,
            title = dto.title,
            imageUrl = dto.images.jpg.largeImageUrl ?: dto.images.jpg.imageUrl,
            score = dto.score,
            episodes = dto.episodes,
            status = dto.status,
            synopsis = dto.synopsis,
            trailerUrl = dto.trailer?.embedUrl,
            genres = dto.genres?.map { it.name } ?: emptyList()
        )
    }
}
