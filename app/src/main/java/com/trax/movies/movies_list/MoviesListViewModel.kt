package com.trax.movies.movies_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trax.data.MoviesRepository
import com.trax.models.DetailedMoviesDomainModel
import com.trax.movies.ui.Event
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.schedulers.Schedulers.io
import timber.log.Timber
import java.util.*

class MoviesListViewModel(
    val moviesRepository: MoviesRepository
) : ViewModel() {
    
    private val _movies = MutableLiveData<DetailedMoviesDomainModel>()
    val movies: LiveData<DetailedMoviesDomainModel> = _movies

    private val _errorWhenFetching = MutableLiveData<Event<Unit>>()
    val errorWhenFetching: LiveData<Event<Unit>> = _errorWhenFetching

    private val compositeDisposable = CompositeDisposable()

    init {
        fetchMovies()
    }

    fun fetchMovies() {
        moviesRepository
            .fetchMovies(Locale.getDefault().language)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = {},
                onError = {
                    _errorWhenFetching.value = Event(Unit)
                    Timber.e(it) })
            .addTo(compositeDisposable)
    }

    fun observeMovies() {
        moviesRepository
            .observeMovies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { _movies.postValue(it) },
                onError = { Timber.e(it) })
            .addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}