package com.trax.movies

import com.trax.movies.detail_movie.DetailMovieViewModel
import com.trax.movies.movies_list.MoviesListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel { MoviesListViewModel(get()) }

    viewModel { DetailMovieViewModel(get()) }

}