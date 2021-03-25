package com.trax.data.remote

import com.trax.data.remote.converter.toDomainModel
import com.trax.data.remote.models.DetailedMovieRemoteModel
import com.trax.data.remote.models.DetailsRemoteModel
import com.trax.data.remote.models.MoviesRemoteModel
import com.trax.data.remote.models.PageRemoteModel
import com.trax.models.DetailedMovieDomainModel.Companion.DEFAULT_RELEASE_DATE
import com.trax.models.DetailedMovieDomainModel.Companion.DEFAULT_RUNTIME
import org.junit.Assert
import org.junit.Test
import java.util.*

class MoviesRemoteDataSourceToMoviesDomainModelConverterTest {

    @Test
    fun takeDefaultId() {
        val remoteMovies = moviesRemoteModel
        val remoteDomainModel = remoteMovies.toDomainModel("en")
        Assert.assertEquals(
            "Id should be at 0 for permitting room to increment himself the id",
            0,
            remoteDomainModel.id
        )
    }

    @Test
    fun parseTitle() {
        val remoteMovies = moviesRemoteModel.copy(
            details = DetailsRemoteModel(
                locale = mutableMapOf(
                    "en" to DetailedMovieRemoteModel(
                        "The Most Dangerous Year",
                        ""
                    )
                ), ""
            )
        )
        val remoteDomainModel = remoteMovies.toDomainModel("en")
        Assert.assertEquals("Parse title", "The Most Dangerous Year", remoteDomainModel.title)
    }

    @Test
    fun parseTitleFallbackOnPage() {
        val remoteMovies = moviesRemoteModel.copy(
            page = PageRemoteModel("The Most Fabulous Year", ""),
            details = DetailsRemoteModel(
                locale = mutableMapOf(
                    "en" to DetailedMovieRemoteModel(
                        null,
                        ""
                    )
                ), ""
            )
        )
        val remoteDomainModel = remoteMovies.toDomainModel("en")
        Assert.assertEquals("Parse title", "The Most Fabulous Year", remoteDomainModel.title)
    }

    @Test
    fun parseSynopsis() {
        val remoteMovies = moviesRemoteModel.copy(
            details = DetailsRemoteModel(
                locale =
                mutableMapOf(
                    "en" to DetailedMovieRemoteModel(
                        "",
                        "In 2016 a small group of families with transgender kids joined the fight against a wave of discriminatory anti-transgender legislation"
                    )
                ), ""
            )
        )

        val remoteDomainModel = remoteMovies.toDomainModel("en")
        Assert.assertEquals(
            "Parse synopsis",
            "In 2016 a small group of families with transgender kids joined the fight against a wave of discriminatory anti-transgender legislation",
            remoteDomainModel.synopsis
        )
    }

    @Test
    fun parseOtherLanguages() {
        val remoteMovies = moviesRemoteModel.copy(
            details = DetailsRemoteModel(
                locale =
                mutableMapOf(
                    "fr" to DetailedMovieRemoteModel(
                        "The Most Fabulous Year",
                        "In 2016 a small group of families with transgender kids joined the fight against a wave of discriminatory anti-transgender legislation"
                    )
                ), ""
            )
        )

        val remoteDomainModel = remoteMovies.toDomainModel("fr")
        Assert.assertEquals(
            "Parse synopsis",
            "The Most Fabulous Year",
            remoteDomainModel.title
        )
        Assert.assertEquals(
            "Parse synopsis",
            "In 2016 a small group of families with transgender kids joined the fight against a wave of discriminatory anti-transgender legislation",
            remoteDomainModel.synopsis
        )
    }

    @Test
    fun parseOtherLanguagesNotAvailableMustFallbackEnglish() {
        val remoteMovies = moviesRemoteModel.copy(
            details = DetailsRemoteModel(
                locale =
                mutableMapOf(
                    "en" to DetailedMovieRemoteModel(
                        "The Most Dangerous Year",
                        "In 2016 a small group of families with transgender kids joined the fight against a wave of discriminatory anti-transgender legislation"
                    )
                ), ""
            )
        )

        val remoteDomainModel = remoteMovies.toDomainModel("fr")
        Assert.assertEquals(
            "Parse synopsis",
            "The Most Dangerous Year",
            remoteDomainModel.title
        )
        Assert.assertEquals(
            "Parse synopsis",
            "In 2016 a small group of families with transgender kids joined the fight against a wave of discriminatory anti-transgender legislation",
            remoteDomainModel.synopsis
        )
    }

    @Test
    fun parseReleaseDate() {
        val remoteMovies = moviesRemoteModel.copy(
            page = PageRemoteModel(
                "The Most Fabulous Year",
                "2019-04-12"
            )
        )
        val remoteDomainModel = remoteMovies.toDomainModel("en")

        val calendar: Calendar = Calendar.getInstance()
        calendar.time = remoteDomainModel.releaseDate

        Assert.assertEquals("Parse correct release date", 12, calendar.get(Calendar.DAY_OF_MONTH))
        Assert.assertEquals("Parse correct release date", 3, calendar.get(Calendar.MONTH))
        Assert.assertEquals("Parse correct release date", 2019, calendar.get(Calendar.YEAR))
    }

    @Test
    fun parseIncorrectReleaseDate() {
        val remoteMovies = moviesRemoteModel.copy(
            page = PageRemoteModel(
                "The Most Fabulous Year",
                "incorrect-release-date"
            )
        )
        val remoteDomainModel = remoteMovies.toDomainModel("en")
        Assert.assertEquals(
            "Parse correct release date",
            DEFAULT_RELEASE_DATE,
            remoteDomainModel.releaseDate
        )
    }

    @Test
    fun parseRuntime() {
        val remoteMovies = moviesRemoteModel.copy(
            details = DetailsRemoteModel(
                locale = mutableMapOf(
                    "en" to DetailedMovieRemoteModel(
                        "",
                        ""
                    )
                ), "1 hour 30 minutes"
            )
        )
        val remoteDomainModel = remoteMovies.toDomainModel("en")
        Assert.assertEquals("Parse correct release date", 90, remoteDomainModel.runtime)
    }

    @Test
    fun parseRuntimeOnlyMinutes() {
        val remoteMovies = moviesRemoteModel.copy(
            details = DetailsRemoteModel(
                locale = mutableMapOf(
                    "en" to DetailedMovieRemoteModel(
                        "",
                        ""
                    )
                ), "30 minutes")
        )
        val remoteDomainModel = remoteMovies.toDomainModel("en")
        Assert.assertEquals("Parse correct release date", 30, remoteDomainModel.runtime)
    }

    @Test
    fun parseRuntime2Hours() {
        val remoteMovies = moviesRemoteModel.copy(
            details = DetailsRemoteModel(
                locale = mutableMapOf(
                    "en" to DetailedMovieRemoteModel(
                        "",
                        ""
                    )
                ), "2 hours 30 minutes"
            )
        )
        val remoteDomainModel = remoteMovies.toDomainModel("en")
        Assert.assertEquals("Parse correct release date", 150, remoteDomainModel.runtime)
    }

    @Test
    fun parseRuntimeSingularMinute() {
        val remoteMovies = moviesRemoteModel.copy(
            details = DetailsRemoteModel(
                locale = mutableMapOf(
                    "en" to DetailedMovieRemoteModel(
                        "",
                        ""
                    )
                ), "2 hours 1 minute"
            )
        )
        val remoteDomainModel = remoteMovies.toDomainModel("en")
        Assert.assertEquals("Parse correct release date", 121, remoteDomainModel.runtime)
    }

    @Test
    fun parseRuntimeOnlyHour() {
        val remoteMovies = moviesRemoteModel.copy(
            details = DetailsRemoteModel(
                locale = mutableMapOf(
                    "en" to DetailedMovieRemoteModel(
                        "",
                        ""
                    )
                ), "2 hours"
            )
        )
        val remoteDomainModel = remoteMovies.toDomainModel("en")
        Assert.assertEquals("Parse correct release date", 120, remoteDomainModel.runtime)
    }

    @Test
    fun parseIncorrectRuntimeOnlyHour() {
        val remoteMovies = moviesRemoteModel.copy(
            details = DetailsRemoteModel(
                locale = mutableMapOf(
                    "en" to DetailedMovieRemoteModel(
                        "",
                        ""
                    )
                ), "incorrect-runtime"
            )
        )
        val remoteDomainModel = remoteMovies.toDomainModel("en")
        Assert.assertEquals(
            "Parse correct release date",
            DEFAULT_RUNTIME,
            remoteDomainModel.runtime
        )
    }

}