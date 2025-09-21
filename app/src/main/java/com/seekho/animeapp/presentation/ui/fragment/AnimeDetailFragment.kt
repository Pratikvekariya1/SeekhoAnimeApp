package com.seekho.animeapp.presentation.ui.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.seekho.animeapp.R
import com.seekho.animeapp.databinding.FragmentAnimeDetailBinding
import com.seekho.animeapp.domain.model.Anime
import com.seekho.animeapp.presentation.state.AnimeDetailUiState
import com.seekho.animeapp.presentation.state.AnimeFavoriteUiState
import com.seekho.animeapp.presentation.ui.fragment.base.BaseFragment
import com.seekho.animeapp.presentation.viewmodel.AnimeDetailViewModel
import com.seekho.animeapp.utils.NetworkStateManager
import com.seekho.animeapp.utils.VideoPlayerManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AnimeDetailFragment : BaseFragment<FragmentAnimeDetailBinding>() {

    private val viewModel: AnimeDetailViewModel by viewModels()
    private val args: AnimeDetailFragmentArgs by navArgs()
    @Inject
    lateinit var videoPlayerManager: VideoPlayerManager

    @Inject
    lateinit var networkStateManager: NetworkStateManager

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAnimeDetailBinding {
        return FragmentAnimeDetailBinding.inflate(inflater, container, false)
    }

    override fun setupViews() {
        viewModel.loadAnimeDetails(args.animeId)

        // Add YouTube player to lifecycle
        lifecycle.addObserver(binding.youtubePlayerView)

    }

    override fun observeViewModel() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AnimeDetailUiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.scrollView.visibility = View.GONE
                }
                is AnimeDetailUiState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.scrollView.visibility = View.VISIBLE
                    bindAnimeDetails(state.anime)
                }
                is AnimeDetailUiState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.favoriteState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AnimeFavoriteUiState.Loading -> {
                    binding.buttonFavorite.visibility = View.GONE
                }
                is AnimeFavoriteUiState.Success -> {
                    binding.buttonFavorite.visibility = View.VISIBLE

                    binding.buttonFavorite.setImageResource(
                        if (state.isFavorite) R.drawable.ic_favorite_filled
                        else R.drawable.ic_favorite_border
                    )
                }
                is AnimeFavoriteUiState.Error -> {
                    binding.buttonFavorite.visibility = View.VISIBLE
                    Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun bindAnimeDetails(anime: Anime) {
        binding.apply {
            textTitle.text = anime.title
            textSynopsis.text = anime.synopsis ?: getString(R.string.no_synopsis_available)
            textEpisodes.text = getString(R.string.episodes_count, anime.episodes ?: "Unknown")
            textRating.text = getString(R.string.rating_score, anime.score?.toString() ?: "N/A")
            textGenres.text = getString(R.string.genres_list, anime.genres.joinToString(", "))
            textStatus.text = getString(R.string.status_text, anime.status ?: "Unknown")

            // Set favorite button
            buttonFavorite.setImageResource(
                if (anime.isFavorite) R.drawable.ic_favorite_filled
                else R.drawable.ic_favorite_border
            )

            buttonFavorite.setOnClickListener {
                viewModel.toggleFavorite(anime.malId)
            }

            // Load anime poster
            Glide.with(this@AnimeDetailFragment)
                .load(anime.imageUrl)
                .placeholder(R.drawable.placeholder_anime)
                .error(R.drawable.error_anime)
                .into(imageAnime)

            // Setup video player based on URL type
            anime.trailerUrl?.let { trailerUrl ->
                setupVideoPlayer(trailerUrl)
            } ?: run {
                showNoTrailerState()
            }
        }
    }

    private fun setupVideoPlayer(trailerUrl: String) {
        binding.apply {
            // Show video container and hide no trailer message
            videoContainer.visibility = View.VISIBLE
            textNoTrailer.visibility = View.GONE


            // Setup the appropriate player - FIXED CALL
            videoPlayerManager.setupPlayer(
                url = trailerUrl,
                youTubePlayerView = youtubePlayerView,
                exoPlayerView = exoPlayerView,
                networkStateManager = networkStateManager,  // Add this parameter
                onError = { errorMessage ->
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                    showNoTrailerState()
                },
                onReady = {
                    // Video is ready to play
                    progressBarVideo.visibility = View.GONE
                },
                onNetworkWarning = {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.mobile_data_warning),
                        Toast.LENGTH_LONG
                    ).show()
                }
            )

            // Show loading while video is being prepared
            progressBarVideo.visibility = View.VISIBLE
        }
    }

    private fun showNoTrailerState() {
        binding.apply {
            videoContainer.visibility = View.GONE
            textNoTrailer.visibility = View.VISIBLE
            progressBarVideo.visibility = View.GONE
        }
    }

    override fun onPause() {
        super.onPause()
        videoPlayerManager.onPause()
    }

    override fun onResume() {
        super.onResume()
        videoPlayerManager.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        videoPlayerManager.releaseAllPlayers()
    }
}
