package com.seekho.animeapp.domain.usecase

import com.seekho.animeapp.domain.repository.AnimeRepository
import com.seekho.animeapp.domain.usecase.base.BaseUseCase
import com.seekho.animeapp.utils.Resource
import javax.inject.Inject

class GetTopAnimeUseCase @Inject constructor(
    private val repository: AnimeRepository
) : BaseUseCase<Unit, Unit>() {

    override suspend fun execute(parameters: Unit): Resource<Unit> {
        return repository.refreshAnimeList()
    }
}