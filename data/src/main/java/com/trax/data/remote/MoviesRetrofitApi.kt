package com.trax.data.remote

import com.trax.data.remote.models.MoviesRemoteModel
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface MoviesRetrofitApi {
    @GET("movies.json")
    fun fetchMovies(): Observable<List<MoviesRemoteModel>>
}