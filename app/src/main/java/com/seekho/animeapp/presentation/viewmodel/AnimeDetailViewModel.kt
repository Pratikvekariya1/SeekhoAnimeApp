package com.seekho.animeapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seekho.animeapp.domain.repository.AnimeRepository
import com.seekho.animeapp.domain.usecase.GetAnimeDetailsUseCase
import com.seekho.animeapp.presentation.state.AnimeDetailUiState
import com.seekho.animeapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeDetailViewModel @Inject constructor(
    private val repository: AnimeRepository,
    private val getAnimeDetailsUseCase: GetAnimeDetailsUseCase
) : ViewModel() {
    
    private val _uiState = MutableLiveData<AnimeDetailUiState>()
    val uiState: LiveData<AnimeDetailUiState> = _uiState
    
    fun loadAnimeDetails(animeId: Int) {
        viewModelScope.launch {
            _uiState.value = AnimeDetailUiState.Loading
            
            when (val result = getAnimeDetailsUseCase(animeId)) {
                is Resource.Success -> {
                    result.data?.let { anime ->
                        _uiState.value = AnimeDetailUiState.Success(anime)
                    }
                }
                is Resource.Error -> {
                    _uiState.value = AnimeDetailUiState.Error(
                        result.message ?: "Unknown error occurred"
                    )
                }
                is Resource.Loading -> {
                    _uiState.value = AnimeDetailUiState.Loading
                }
            }
        }
    }
    
    fun toggleFavorite(animeId: Int) {
        viewModelScope.launch {
            repository.toggleFavorite(animeId)
        }
    }
}
