package com.seekho.animeapp.presentation.ui.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.seekho.animeapp.R
import com.seekho.animeapp.databinding.FragmentAnimeListBinding
import com.seekho.animeapp.presentation.state.AnimeListUiState
import com.seekho.animeapp.presentation.ui.adapter.AnimeListAdapter
import com.seekho.animeapp.presentation.ui.fragment.base.BaseFragment
import com.seekho.animeapp.presentation.viewmodel.AnimeListViewModel
import com.seekho.animeapp.utils.NetworkStateManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnimeListFragment : BaseFragment<FragmentAnimeListBinding>() {

    private val viewModel: AnimeListViewModel by viewModels()
    private lateinit var adapter: AnimeListAdapter
    private var networkSnackbar: Snackbar? = null

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAnimeListBinding {
        return FragmentAnimeListBinding.inflate(inflater, container, false)
    }

    override fun setupViews() {
        setupRecyclerView()
        setupSwipeRefresh()
        setupNetworkIndicator()
    }

    private fun setupRecyclerView() {
        adapter = AnimeListAdapter(
            onAnimeClick = { anime ->
                findNavController().navigate(
                    AnimeListFragmentDirections.actionListToDetail(anime.malId)
                )
            },
            onFavoriteClick = { anime ->
                viewModel.toggleFavorite(anime.malId)
            }
        )

        binding.recyclerViewAnime.apply {
            adapter = this@AnimeListFragment.adapter
            layoutManager = GridLayoutManager(context, 2)
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshAnimeList()
        }
    }

    private fun setupNetworkIndicator() {
        binding.buttonRetry.setOnClickListener {
            viewModel.retryConnection()
        }
    }

    override fun observeViewModel() {
        // Observe anime list state
        viewModel.animeList.observe(viewLifecycleOwner) { state ->
            handleUiState(state)
        }

        // Observe loading/error state
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AnimeListUiState.Error -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    if (!state.message.contains("cached", ignoreCase = true)) {
                        Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                    }
                }
                is AnimeListUiState.Loading -> {
                    // Loading handled by swipe refresh
                }
                else -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }
        }

        // Observe network state
        viewModel.isConnected.observe(viewLifecycleOwner) { isConnected ->
            updateNetworkIndicator(isConnected)
        }

        // Observe network type for data usage warnings
        viewModel.networkType.observe(viewLifecycleOwner) { networkType ->
            if (networkType == NetworkStateManager.NetworkType.CELLULAR) {
                showDataUsageWarning()
            }
        }

        // Observe offline mode
        viewModel.isOfflineMode.observe(viewLifecycleOwner) { isOffline ->
            binding.textOfflineMode.visibility = if (isOffline) View.VISIBLE else View.GONE
        }

        // Observe network errors
        viewModel.showNetworkError.observe(viewLifecycleOwner) { showError ->
            if (showError) {
                showNetworkErrorSnackbar()
            } else {
                networkSnackbar?.dismiss()
            }
        }
    }

    private fun handleUiState(state: AnimeListUiState) {
        when (state) {
            is AnimeListUiState.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
                binding.recyclerViewAnime.visibility = View.GONE
                binding.layoutError.visibility = View.GONE
            }
            is AnimeListUiState.Success -> {
                binding.progressBar.visibility = View.GONE
                binding.recyclerViewAnime.visibility = View.VISIBLE
                binding.layoutError.visibility = View.GONE
                binding.swipeRefreshLayout.isRefreshing = false
                adapter.submitList(state.animeList)
            }
            is AnimeListUiState.Empty -> {
                binding.progressBar.visibility = View.GONE
                binding.recyclerViewAnime.visibility = View.GONE
                binding.layoutError.visibility = View.VISIBLE
                binding.textError.text = getString(R.string.no_anime_available)
                binding.buttonRetry.visibility = View.VISIBLE
                binding.swipeRefreshLayout.isRefreshing = false
            }
            is AnimeListUiState.Error -> {
                binding.progressBar.visibility = View.GONE
                binding.recyclerViewAnime.visibility = View.GONE
                binding.layoutError.visibility = View.VISIBLE
                binding.textError.text = state.message
                binding.buttonRetry.visibility = View.VISIBLE
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun updateNetworkIndicator(isConnected: Boolean) {
        binding.imageNetworkStatus.setImageResource(
            if (isConnected) R.drawable.ic_wifi_on else R.drawable.ic_wifi_off
        )
        binding.imageNetworkStatus.visibility = View.VISIBLE
    }

    private fun showNetworkErrorSnackbar() {
        networkSnackbar = Snackbar.make(
            binding.root,
            "No internet connection",
            Snackbar.LENGTH_INDEFINITE
        ).setAction("Retry") {
            viewModel.retryConnection()
        }
        networkSnackbar?.show()
    }

    private fun showDataUsageWarning() {
        if (isAdded) {
            Snackbar.make(
                binding.root,
                "Using mobile data. Video streaming may consume data.",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        networkSnackbar?.dismiss()
    }
}
