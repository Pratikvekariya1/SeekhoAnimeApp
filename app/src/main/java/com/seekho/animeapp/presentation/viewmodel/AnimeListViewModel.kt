package com.seekho.animeapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.seekho.animeapp.domain.repository.AnimeRepository
import com.seekho.animeapp.domain.usecase.GetTopAnimeUseCase
import com.seekho.animeapp.presentation.state.AnimeListUiState
import com.seekho.animeapp.utils.NetworkStateManager
import com.seekho.animeapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimeListViewModel @Inject constructor(
    private val repository: AnimeRepository,
    private val getTopAnimeUseCase: GetTopAnimeUseCase,
    private val networkStateManager: NetworkStateManager
) : ViewModel() {
    
    private val _uiState = MutableLiveData<AnimeListUiState>(AnimeListUiState.Loading)
    val uiState: LiveData<AnimeListUiState> = _uiState
    
    private val _showNetworkError = MutableLiveData<Boolean>()
    val showNetworkError: LiveData<Boolean> = _showNetworkError

    private val _isOfflineMode = MutableLiveData<Boolean>()
    val isOfflineMode: LiveData<Boolean> = _isOfflineMode

    // Observe network state
    val isConnected: LiveData<Boolean> = networkStateManager.isConnected
    val networkType: LiveData<NetworkStateManager.NetworkType> = networkStateManager.networkType

    val animeList = repository.getAllAnime().map { animeList ->
        if (animeList.isEmpty()) {
            if (networkStateManager.isNetworkAvailable()) {
                AnimeListUiState.Empty
            } else {
                _isOfflineMode.value = true
                AnimeListUiState.Error("No cached data available. Please connect to internet.")
            }
        } else {
            _isOfflineMode.value = !networkStateManager.isNetworkAvailable()
            AnimeListUiState.Success(animeList)
        }
    }
    
    init {
        // Auto-refresh when network becomes available
        networkStateManager.isConnected.observeForever { connected ->
            if (connected) {
                _showNetworkError.value = false
                refreshAnimeListIfNeeded()
            } else {
                _showNetworkError.value = true
            }
        }

        refreshAnimeList()
    }
    
    fun refreshAnimeList() {
        viewModelScope.launch {
            if (!networkStateManager.isNetworkAvailable()) {
                _uiState.value = AnimeListUiState.Error("No internet connection. Showing cached data.")
                return@launch
            }

            _uiState.value = AnimeListUiState.Loading
            
            when (val result = getTopAnimeUseCase(Unit)) {
                is Resource.Success -> {
                    // UI state will be updated through animeList LiveData
                    _showNetworkError.value = false
                }
                is Resource.Error -> {
                    _uiState.value = AnimeListUiState.Error(
                        result.message ?: "Unknown error occurred"
                    )
                    if (result.message?.contains("internet", ignoreCase = true) == true) {
                        _showNetworkError.value = true
                    }
                }
                is Resource.Loading -> {
                    _uiState.value = AnimeListUiState.Loading
                }
            }
        }
    }
    
    private fun refreshAnimeListIfNeeded() {
        // Only refresh if we don't have cached data or if explicitly requested
        if (repository.getAllAnime().value.isNullOrEmpty()) {
            refreshAnimeList()
        }
    }

    fun retryConnection() {
        if (networkStateManager.isNetworkAvailable()) {
            refreshAnimeList()
        }
    }

    fun toggleFavorite(animeId: Int) {
        viewModelScope.launch {
            repository.toggleFavorite(animeId)
        }
    }

    fun dismissNetworkError() {
        _showNetworkError.value = false
    }
}
