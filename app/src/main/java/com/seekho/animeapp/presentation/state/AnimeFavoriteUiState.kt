package com.seekho.animeapp.presentation.state

sealed class AnimeFavoriteUiState {

    object Loading : AnimeFavoriteUiState()
    data class Success(val isFavorite: Boolean) : AnimeFavoriteUiState()
    data class Error(val message: String) : AnimeFavoriteUiState()
}
