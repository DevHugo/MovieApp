package com.trax.movies.detail_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.trax.data.MoviesRepository
import com.trax.models.DetailedMovieDomainModel
import com.trax.models.DetailedMoviesDomainModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.schedulers.Schedulers.io
import timber.log.Timber

class DetailMovieViewModel(
    val moviesRepository: MoviesRepository
    ) : ViewModel() {
    
    private val _movie = MutableLiveData<DetailedMovieDomainModel>()
    val movie: LiveData<DetailedMovieDomainModel> = _movie

    private val compositeDisposable = CompositeDisposable()

    fun fetchMovie(movieId: Int) {
        moviesRepository
            .observeMovie(movieId)
            .subscribeOn(io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { _movie.value = it },
                onError = { Timber.e(it) })
            .addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}