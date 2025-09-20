package com.seekho.animeapp.presentation.state

import com.seekho.animeapp.domain.model.Anime

sealed class AnimeDetailUiState {
    object Loading : AnimeDetailUiState()
    data class Success(val anime: Anime) : AnimeDetailUiState()
    data class Error(val message: String) : AnimeDetailUiState()
}
