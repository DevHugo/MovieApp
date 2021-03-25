package com.trax.data

import com.trax.models.DetailedMovieDomainModel
import com.trax.models.DetailedMoviesDomainModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

interface MoviesRepository {

    fun fetchMovies(language: String): Completable
    fun observeMovie(movieId: Int): Observable<DetailedMovieDomainModel>
    fun observeMovies(): Observable<DetailedMoviesDomainModel>
}