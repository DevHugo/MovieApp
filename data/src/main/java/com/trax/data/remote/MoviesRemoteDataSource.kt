package com.trax.data.remote

import com.trax.models.DetailedMoviesDomainModel
import io.reactivex.rxjava3.core.Observable

interface MoviesRemoteDataSource {

    fun fetchRemote(language: String): Observable<DetailedMoviesDomainModel>
}