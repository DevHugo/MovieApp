package com.trax.data

import com.trax.data.local.MoviesLocalDataSource
import com.trax.data.remote.MoviesRemoteDataSource
import com.trax.models.DetailedMovieDomainModel
import com.trax.models.DetailedMovieDomainModel.Companion.DEFAULT_ID
import com.trax.models.DetailedMovieDomainModel.Companion.DEFAULT_PICTURE_URL
import com.trax.models.DetailedMovieDomainModel.Companion.DEFAULT_RELEASE_DATE
import com.trax.models.DetailedMovieDomainModel.Companion.DEFAULT_RUNTIME
import com.trax.models.DetailedMovieDomainModel.Companion.DEFAULT_SYNOPSIS
import com.trax.models.DetailedMovieDomainModel.Companion.DEFAULT_TITLE
import com.trax.models.DetailedMovieDomainModel.Companion.DEFAULT_TRAILER_URL
import com.trax.models.DetailedMoviesDomainModel
import io.mockk.called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import org.junit.Test
import java.util.concurrent.TimeUnit

class MoviesRepositoryImplTest {

    @Test
    fun fetchMoviesFirstTimeShouldTriggerTheLocalDataSource() {
        val movies = DetailedMoviesDomainModel(listOf(DetailedMovieDomainModel(
            DEFAULT_ID, DEFAULT_TITLE, DEFAULT_SYNOPSIS, DEFAULT_PICTURE_URL,
            DEFAULT_RUNTIME, DEFAULT_RELEASE_DATE, DEFAULT_TRAILER_URL)))

        val movieRemoteDataSource = mockk<MoviesRemoteDataSource>()
        every { movieRemoteDataSource.fetchRemote("en") } returns Observable.just(movies)

        val movieLocalDataSource = mockk<MoviesLocalDataSource>()
        every { movieLocalDataSource.saveMovies(movies) } returns Completable.complete()
        every { movieLocalDataSource.lastTimeMoviesFetched() } returns 0
        every { movieLocalDataSource.setLastTimeFetchedMovies(any()) } answers {}

        MoviesRepositoryImpl(movieLocalDataSource, movieRemoteDataSource)
            .fetchMovies("en")
            .test()
            .assertComplete()

        verify { movieRemoteDataSource.fetchRemote("en") }
        verify { movieLocalDataSource.saveMovies(movies) }
    }

    @Test
    fun fetchMoviesSecondTimeShouldNotTriggerTheLocalDataSource() {
        val movieRemoteDataSource = mockk<MoviesRemoteDataSource>()

        val movieLocalDataSource = mockk<MoviesLocalDataSource>()
        every { movieLocalDataSource.lastTimeMoviesFetched() } returns System.currentTimeMillis()
        every { movieLocalDataSource.setLastTimeFetchedMovies(any()) } answers {}

        MoviesRepositoryImpl(movieLocalDataSource, movieRemoteDataSource)
            .fetchMovies("en")
            .test()
            .assertComplete()

        verify { movieRemoteDataSource wasNot called }
    }

    @Test
    fun fetchMoviesAfterCacheExpiredShouldTriggerTheLocalDataSource() {
        val movies = DetailedMoviesDomainModel(listOf(DetailedMovieDomainModel(
            DEFAULT_ID, DEFAULT_TITLE, DEFAULT_SYNOPSIS, DEFAULT_PICTURE_URL,
            DEFAULT_RUNTIME, DEFAULT_RELEASE_DATE, DEFAULT_TRAILER_URL)))

        val movieRemoteDataSource = mockk<MoviesRemoteDataSource>()
        every { movieRemoteDataSource.fetchRemote("en") } returns Observable.just(movies)

        val movieLocalDataSource = mockk<MoviesLocalDataSource>()
        every { movieLocalDataSource.saveMovies(movies) } returns Completable.complete()
        every { movieLocalDataSource.lastTimeMoviesFetched() } returns System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(15)
        every { movieLocalDataSource.setLastTimeFetchedMovies(any()) } answers {}

        MoviesRepositoryImpl(movieLocalDataSource, movieRemoteDataSource)
            .fetchMovies("en")
            .test()
            .assertComplete()

        verify { movieRemoteDataSource.fetchRemote("en") }
        verify { movieLocalDataSource.saveMovies(movies) }
    }
}