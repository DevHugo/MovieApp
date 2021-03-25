package com.trax.data.local

import com.trax.data.local.converter.toDomainModel
import com.trax.data.local.converter.toEntity
import com.trax.models.DetailedMovieDomainModel
import com.trax.models.DetailedMoviesDomainModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

class MoviesLocalDataSourceImpl (val db: MoviesDatabase): MoviesLocalDataSource {

    private var lastTimeMoviesFetched = 0L

    override fun fetchMovies(): Observable<DetailedMoviesDomainModel> =
        db.moviesDao().getAll().map { DetailedMoviesDomainModel(it.toDomainModel()) }

    override fun saveMovies(movies: DetailedMoviesDomainModel): Completable {
        val moviesEntity = movies.movies.toEntity()
        return db.moviesDao().deleteAll()
                    .andThen(db.moviesDao().insertAll(moviesEntity))
    }

    override fun fetchMovie(movieId: Int): Observable<DetailedMovieDomainModel> =
        db.moviesDao().getOne(movieId).map { it.toDomainModel() }

    override fun lastTimeMoviesFetched(): Long = lastTimeMoviesFetched

    override fun setLastTimeFetchedMovies(currentTimeMillis: Long) {
        lastTimeMoviesFetched = currentTimeMillis
    }
}