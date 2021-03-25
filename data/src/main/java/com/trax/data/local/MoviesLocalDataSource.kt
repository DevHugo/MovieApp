package com.trax.data.local

import com.trax.models.DetailedMovieDomainModel
import com.trax.models.DetailedMoviesDomainModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

interface MoviesLocalDataSource {
    fun fetchMovies(): Observable<DetailedMoviesDomainModel>
    fun saveMovies(movies: DetailedMoviesDomainModel): Completable
    fun fetchMovie(movieId: Int): Observable<DetailedMovieDomainModel>

    fun lastTimeMoviesFetched(): Long
    fun setLastTimeFetchedMovies(currentTimeMillis: Long)
}