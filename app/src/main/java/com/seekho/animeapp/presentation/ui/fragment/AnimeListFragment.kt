package com.seekho.animeapp.presentation.ui.fragment

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.seekho.animeapp.databinding.FragmentAnimeListBinding
import com.seekho.animeapp.presentation.state.AnimeListUiState
import com.seekho.animeapp.presentation.ui.adapter.AnimeListAdapter
import com.seekho.animeapp.presentation.ui.fragment.base.BaseFragment
import com.seekho.animeapp.presentation.viewmodel.AnimeListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnimeListFragment : BaseFragment<FragmentAnimeListBinding>() {
    private val TAG = "AnimeListFragment"

    private val viewModel: AnimeListViewModel by viewModels()
    private lateinit var adapter: AnimeListAdapter

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAnimeListBinding {
        return FragmentAnimeListBinding.inflate(inflater, container, false)
    }

    override fun setupViews() {
        setupRecyclerView()
        setupSwipeRefresh()
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

    override fun observeViewModel() {
        viewModel.animeList.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AnimeListUiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.recyclerViewAnime.visibility = View.GONE
                    binding.textEmpty.visibility = View.GONE
                }
                is AnimeListUiState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerViewAnime.visibility = View.VISIBLE
                    binding.textEmpty.visibility = View.GONE
                    binding.swipeRefreshLayout.isRefreshing = false
                    adapter.submitList(state.animeList)
                }
                is AnimeListUiState.Empty -> {
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerViewAnime.visibility = View.GONE
                    binding.textEmpty.visibility = View.VISIBLE
                    binding.swipeRefreshLayout.isRefreshing = false
                }
                is AnimeListUiState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.swipeRefreshLayout.isRefreshing = false
                    Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AnimeListUiState.Error -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                }
                is AnimeListUiState.Loading -> {
                    // Handle loading state if needed
                    Log.d(TAG, "observeViewModel uiState: Loading")
                }
                else -> {
                    // Handle other states
                    Log.d(TAG, "observeViewModel uiState: $state")
                }
            }
        }
    }
}