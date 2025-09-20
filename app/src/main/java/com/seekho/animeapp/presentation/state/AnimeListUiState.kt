package com.seekho.animeapp.presentation.state

import com.seekho.animeapp.domain.model.Anime

sealed class AnimeListUiState {
    object Loading : AnimeListUiState()
    data class Success(val animeList: List<Anime>) : AnimeListUiState()
    data class Error(val message: String) : AnimeListUiState()
    object Empty : AnimeListUiState()
}
