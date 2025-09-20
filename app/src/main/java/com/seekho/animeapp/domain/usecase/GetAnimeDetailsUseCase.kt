package com.seekho.animeapp.domain.usecase

import com.seekho.animeapp.domain.model.Anime
import com.seekho.animeapp.domain.repository.AnimeRepository
import com.seekho.animeapp.domain.usecase.base.BaseUseCase
import com.seekho.animeapp.utils.Resource
import javax.inject.Inject

class GetAnimeDetailsUseCase @Inject constructor(
    private val repository: AnimeRepository
) : BaseUseCase<Int, Anime>() {
    
    override suspend fun execute(parameters: Int): Resource<Anime> {
        return repository.getAnimeDetails(parameters)
    }
}
