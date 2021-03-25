package com.trax.movies.movies_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.trax.models.MovieDomainModel
import com.trax.movies.databinding.RowitemMovieBinding
import timber.log.Timber

class MoviesAdapter(private val onClickListener: OnClickOnMovieListener) : ListAdapter<MovieDomainModel, MoviesAdapter.ViewHolder>(MovieDiffCallback()) {

    class MovieDiffCallback : DiffUtil.ItemCallback<MovieDomainModel>() {
        override fun areItemsTheSame(oldItem: MovieDomainModel, newItem: MovieDomainModel): Boolean {
            return oldItem.id == newItem.id;
        }

        override fun areContentsTheSame(oldItem: MovieDomainModel, newItem: MovieDomainModel): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesAdapter.ViewHolder {
        val binding = RowitemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MoviesAdapter.ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it, onClickListener)
        }
    }

    class ViewHolder(private val binding: RowitemMovieBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MovieDomainModel, onClickListener: OnClickOnMovieListener) {
            if (item.pictureUrl.isNotEmpty()) {
                Picasso.get().load(item.pictureUrl).fit().into(binding.moviesListMovieImage)
            }

            binding.moviesListMovieTitle.text = item.title
            binding.root.setOnClickListener {
                onClickListener.onClick(it, item)
            }
        }
    }
}

interface OnClickOnMovieListener {
    fun onClick(view: View, movie: MovieDomainModel)
}