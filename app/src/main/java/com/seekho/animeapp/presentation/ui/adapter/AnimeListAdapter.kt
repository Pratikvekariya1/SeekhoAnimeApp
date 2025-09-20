package com.seekho.animeapp.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.seekho.animeapp.R
import com.seekho.animeapp.databinding.ItemAnimeBinding
import com.seekho.animeapp.domain.model.Anime

class AnimeListAdapter(
    private val onAnimeClick: (Anime) -> Unit,
    private val onFavoriteClick: (Anime) -> Unit
) : ListAdapter<Anime, AnimeListAdapter.AnimeViewHolder>(DiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolder {
        val binding = ItemAnimeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AnimeViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: AnimeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class AnimeViewHolder(
        private val binding: ItemAnimeBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(anime: Anime) {
            binding.apply {
                textTitle.text = anime.title
                textEpisodes.text = root.context.getString(
                    R.string.episodes_count, 
                    anime.episodes ?: "Unknown"
                )
                textRating.text = root.context.getString(
                    R.string.rating_score,
                    anime.score?.toString() ?: "N/A"
                )
                
                // Set favorite icon
                buttonFavorite.setImageResource(
                    if (anime.isFavorite) R.drawable.ic_favorite_filled 
                    else R.drawable.ic_favorite_border
                )
                
                Glide.with(itemView.context)
                    .load(anime.imageUrl)
                    .placeholder(R.drawable.placeholder_anime)
                    .error(R.drawable.error_anime)
                    .into(imageAnime)
                
                root.setOnClickListener {
                    onAnimeClick(anime)
                }
                
                buttonFavorite.setOnClickListener {
                    onFavoriteClick(anime)
                }
            }
        }
    }
    
    private class DiffCallback : DiffUtil.ItemCallback<Anime>() {
        override fun areItemsTheSame(oldItem: Anime, newItem: Anime): Boolean {
            return oldItem.malId == newItem.malId
        }
        
        override fun areContentsTheSame(oldItem: Anime, newItem: Anime): Boolean {
            return oldItem == newItem
        }
    }
}
