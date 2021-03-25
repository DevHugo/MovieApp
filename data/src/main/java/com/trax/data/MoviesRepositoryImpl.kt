package com.trax.data

import com.trax.data.local.MoviesLocalDataSource
import com.trax.data.remote.MoviesRemoteDataSource
import com.trax.models.DetailedMovieDomainModel
import com.trax.models.DetailedMoviesDomainModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

class MoviesRepositoryImpl(
    val localDataSource: MoviesLocalDataSource,
    val remoteDataSource: MoviesRemoteDataSource) : MoviesRepository {

    private val CACHE_MINUTES = TimeUnit.MINUTES.toMillis(10)

    override fun fetchMovies(language: String): Completable =
        // Fetch data every 10 minutes
        if (System.currentTimeMillis() > CACHE_MINUTES + localDataSource.lastTimeMoviesFetched()) {
            remoteDataSource
                .fetchRemote(language)
                .flatMapCompletable {
                    localDataSource.saveMovies(it)
                }
                .doOnComplete {
                    localDataSource.setLastTimeFetchedMovies(System.currentTimeMillis())
                }
        } else {
            Completable.complete()
        }

    override fun observeMovie(movieId: Int): Observable<DetailedMovieDomainModel> =
        localDataSource.fetchMovie(movieId)

    override fun observeMovies(): Observable<DetailedMoviesDomainModel> =
        localDataSource.fetchMovies()
}