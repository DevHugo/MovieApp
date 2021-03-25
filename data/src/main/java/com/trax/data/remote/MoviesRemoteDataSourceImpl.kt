package com.trax.data.remote

import com.trax.data.remote.converter.toDomainModel
import com.trax.models.DetailedMoviesDomainModel
import io.reactivex.rxjava3.core.Observable
import retrofit2.Retrofit

class MoviesRemoteDataSourceImpl(
    val retrofit: Retrofit,
) : MoviesRemoteDataSource {

    private val service = retrofit.create(MoviesRetrofitApi::class.java)

    override fun fetchRemote(language: String): Observable<DetailedMoviesDomainModel> =
        service.fetchMovies().map {
            it.toDomainModel(language)
        }
}