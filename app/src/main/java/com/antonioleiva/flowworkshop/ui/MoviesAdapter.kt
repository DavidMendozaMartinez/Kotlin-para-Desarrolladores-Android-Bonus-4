package com.antonioleiva.flowworkshop.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.antonioleiva.flowworkshop.R
import com.antonioleiva.flowworkshop.data.domain.Movie
import com.antonioleiva.flowworkshop.databinding.ViewMovieBinding
import com.antonioleiva.flowworkshop.ui.common.onClickEvents
import com.bumptech.glide.Glide
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect

@ExperimentalCoroutinesApi
class MoviesAdapter(
    private val scope: LifecycleCoroutineScope
) : ListAdapter<Movie, MoviesAdapter.ItemViewholder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewholder {
        return ItemViewholder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.view_movie, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewholder, position: Int) {
        val item = getItem(position)
        holder.bind(item)

        scope.launchWhenStarted {
            holder.itemView.onClickEvents.collect {
                Toast.makeText(it.context, item.title, Toast.LENGTH_SHORT).show()
            }
        }
    }

    class ItemViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ViewMovieBinding.bind(itemView)

        fun bind(item: Movie) = with(binding) {
            movieTitle.text = item.title
            Glide
                .with(movieCover.context)
                .load("https://image.tmdb.org/t/p/w185/${item.posterPath}")
                .into(movieCover)
        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}