package com.trax.data.local

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.trax.models.DetailedMovieDomainModel
import com.trax.models.DetailedMoviesDomainModel
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class MoviesLocalDataSourceImplTest {

    private lateinit var moviesDatabase: MoviesDatabase

    @Before
    fun initDb() {
        val instrumentationContext = InstrumentationRegistry.getInstrumentation().context
        moviesDatabase = Room.inMemoryDatabaseBuilder(instrumentationContext, MoviesDatabase::class.java).build()
    }

    @Test
    fun fetchMovies() {
        val movieEntity = MovieEntity(id = 0,
            title = "A super test", synopsis = "A super synopsis", pictureUrl = "",
            runtime = 90, releaseDate = Date(), trailerUrl = "")

        moviesDatabase.moviesDao().insertAll(listOf(movieEntity)).test().assertComplete()

        val movie = MoviesLocalDataSourceImpl(moviesDatabase).fetchMovies().blockingFirst()
        Assert.assertEquals(1, movie.movies.size)
    }

    @Test
    fun fetchMovie() {
        val movieEntity = MovieEntity(id = 2,
            title = "A super test", synopsis = "A super synopsis", pictureUrl = "",
            runtime = 90, releaseDate = Date(), trailerUrl = "")

        moviesDatabase.moviesDao().insertAll(listOf(movieEntity)).test().assertComplete()

        val movie = MoviesLocalDataSourceImpl(moviesDatabase).fetchMovie(2).blockingFirst()
        Assert.assertEquals(2, movie.id)
    }

    @Test
    fun saveMovies() {
        val movieDomainModel = DetailedMoviesDomainModel(movies = listOf(DetailedMovieDomainModel(
            1, "title", "synopsis", "pictureUrl", 1, Date(), "")))

        MoviesLocalDataSourceImpl(moviesDatabase).saveMovies(movieDomainModel).test().assertComplete()

        val movie = MoviesLocalDataSourceImpl(moviesDatabase).fetchMovie(1).blockingFirst()
        Assert.assertEquals(1, movie.id)
    }

    @After
    fun closeDb() {
        moviesDatabase.close()
    }
}