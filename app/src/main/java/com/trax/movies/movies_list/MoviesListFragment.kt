package com.trax.movies.movies_list

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.trax.models.MovieDomainModel
import com.trax.movies.R
import com.trax.movies.databinding.FragmentMoviesListBinding.bind
import com.trax.movies.ui.consume
import com.trax.movies.ui.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class MoviesListFragment : Fragment(R.layout.fragment_movies_list) {

    private val moviesListBinding by viewBinding(::bind)

    val myViewModel: MoviesListViewModel by viewModel()

    private val moviesAdapter = MoviesAdapter(object: OnClickOnMovieListener {
        override fun onClick(view: View, movie: MovieDomainModel) {
            val action = MoviesListFragmentDirections.actionMoviesListToMoviesDetail(movie.id)
            view.findNavController().navigate(action)
        }
    })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAvatar()

        moviesListBinding.fragmentMovieList.adapter = moviesAdapter
        moviesListBinding.fragmentMovieList.layoutManager = GridLayoutManager(view.context, 2)

        myViewModel.observeMovies()
        myViewModel.movies.observe(viewLifecycleOwner, {
            moviesAdapter.submitList(it.movies.map { MovieDomainModel(it.id, it.title, it.pictureUrl ) })
        })

        myViewModel.errorWhenFetching.consume(viewLifecycleOwner, {
            val snackbar = Snackbar.make(moviesListBinding.myCoordinatorLayout, "Error when fetching data", Snackbar.LENGTH_LONG)
            snackbar.setAction("Try again") {
                myViewModel.fetchMovies()
            }
            snackbar.show()
        })

    }

    private fun setAvatar() {
       Picasso
            .get()
            .load(R.drawable.hugo)
            .into(moviesListBinding.fragmentMovieListAvatar, object : Callback {
                override fun onSuccess() {
                    val imageBitmap = (moviesListBinding.fragmentMovieListAvatar.drawable as BitmapDrawable).bitmap
                    val imageDrawable = RoundedBitmapDrawableFactory.create(resources, imageBitmap)
                    imageDrawable.isCircular = true
                    imageDrawable.cornerRadius = imageBitmap.width.coerceAtLeast(imageBitmap.height) / 2.0f
                    moviesListBinding.fragmentMovieListAvatar.setImageDrawable(imageDrawable)
                }

                override fun onError(e: Exception?) {
                    moviesListBinding.fragmentMovieListAvatar.visibility = View.GONE
                }
            })
    }
}