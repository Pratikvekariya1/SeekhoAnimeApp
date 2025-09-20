package com.seekho.animeapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.map
import com.seekho.animeapp.domain.repository.AnimeRepository
import com.seekho.animeapp.domain.usecase.GetTopAnimeUseCase
import com.seekho.animeapp.presentation.state.AnimeListUiState
import com.seekho.animeapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeListViewModel @Inject constructor(
    private val repository: AnimeRepository,
    private val getTopAnimeUseCase: GetTopAnimeUseCase
) : ViewModel() {
    
    private val _uiState = MutableLiveData<AnimeListUiState>(AnimeListUiState.Loading)
    val uiState: LiveData<AnimeListUiState> = _uiState
    
    val animeList = repository.getAllAnime().map { animeList ->
        if (animeList.isEmpty()) {
            AnimeListUiState.Empty
        } else {
            AnimeListUiState.Success(animeList)
        }
    }
    
    init {
        refreshAnimeList()
    }
    
    fun refreshAnimeList() {
        viewModelScope.launch {
            _uiState.value = AnimeListUiState.Loading
            
            when (val result = getTopAnimeUseCase(Unit)) {
                is Resource.Success -> {
                    // UI state will be updated through animeList LiveData
                }
                is Resource.Error -> {
                    _uiState.value = AnimeListUiState.Error(
                        result.message ?: "Unknown error occurred"
                    )
                }
                is Resource.Loading -> {
                    _uiState.value = AnimeListUiState.Loading
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
