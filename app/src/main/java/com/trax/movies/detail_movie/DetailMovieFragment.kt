package com.trax.movies.detail_movie

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.squareup.picasso.Picasso
import com.trax.models.DetailedMovieDomainModel.Companion.DEFAULT_RELEASE_DATE
import com.trax.models.DetailedMovieDomainModel.Companion.DEFAULT_RUNTIME
import com.trax.models.DetailedMovieDomainModel.Companion.DEFAULT_TRAILER_URL
import com.trax.movies.R
import com.trax.movies.databinding.FragmentDetailMovieBinding.bind
import com.trax.movies.ui.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*


class DetailMovieFragment : Fragment(R.layout.fragment_detail_movie) {

    private val moviesDetailBinding by viewBinding(::bind)

    val myViewModel: DetailMovieViewModel by viewModel()

    companion object {
        val ARG_MOVIE_ID = "movie_id"
    }

    private var movieId: Int = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        restoreState(savedInstanceState)

        if (movieId == -1) {
            // Something went really wrong, display an error page
        } else {
            myViewModel.fetchMovie(movieId)
            myViewModel.movie.observe(viewLifecycleOwner, { movie ->
                Picasso.get().load(movie.pictureUrl).fit()
                    .into(moviesDetailBinding.detailMovieImage)
                moviesDetailBinding.detailMovieTitle.text = movie.title
                moviesDetailBinding.detailMovieSynopsis.text = movie.synopsis

                if (movie.releaseDate != DEFAULT_RELEASE_DATE) {
                    moviesDetailBinding.detailMovieReleaseDate.text =
                        SimpleDateFormat("YYYY", Locale.getDefault()).format(movie.releaseDate)
                } else {
                    moviesDetailBinding.detailMovieReleaseDate.text = "Coming soon"
                }

                if (movie.runtime != DEFAULT_RUNTIME) {
                    moviesDetailBinding.detailMovieRuntime.text = "${movie.runtime} minutes"
                }

                if (movie.trailerUrl != DEFAULT_TRAILER_URL) {
                    moviesDetailBinding.detailMovieButton.setOnClickListener {
                        val action = DetailMovieFragmentDirections.actionMoviesDetailToVideoPlayer(movie.trailerUrl)
                        view.findNavController().navigate(action)
                    }
                } else {
                    moviesDetailBinding.detailMovieButton.visibility = View.GONE
                    moviesDetailBinding.detailMovieButton.setOnClickListener {}
                }

                moviesDetailBinding.detailMovieBack.setOnClickListener {
                    activity?.onBackPressed()
                }
            })
        }
    }

    private fun restoreState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null && savedInstanceState.containsKey(ARG_MOVIE_ID)) {
            movieId = savedInstanceState.getInt(ARG_MOVIE_ID)
        }

        arguments?.apply {
            movieId = DetailMovieFragmentArgs.fromBundle(this).movieId
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(ARG_MOVIE_ID, movieId)
    }

}