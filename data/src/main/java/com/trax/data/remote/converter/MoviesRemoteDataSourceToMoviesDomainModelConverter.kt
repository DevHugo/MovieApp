package com.trax.data.remote.converter

import com.trax.data.remote.models.MoviesRemoteModel
import com.trax.models.DetailedMovieDomainModel
import com.trax.models.DetailedMovieDomainModel.Companion.DEFAULT_ID
import com.trax.models.DetailedMovieDomainModel.Companion.DEFAULT_PICTURE_URL
import com.trax.models.DetailedMovieDomainModel.Companion.DEFAULT_RELEASE_DATE
import com.trax.models.DetailedMovieDomainModel.Companion.DEFAULT_RUNTIME
import com.trax.models.DetailedMovieDomainModel.Companion.DEFAULT_SYNOPSIS
import com.trax.models.DetailedMovieDomainModel.Companion.DEFAULT_TITLE
import com.trax.models.DetailedMovieDomainModel.Companion.DEFAULT_TRAILER_URL
import com.trax.models.DetailedMoviesDomainModel
import java.text.SimpleDateFormat
import java.util.*

fun List<MoviesRemoteModel>.toDomainModel(language: String) =
    DetailedMoviesDomainModel(map { it.toDomainModel(language) })

fun MoviesRemoteModel.toDomainModel(language: String): DetailedMovieDomainModel {
    var details = this.details?.locale?.getOrElse(language, {null})

    // fallback by default in english for the movie title or synopsis
    if (details == null)
        details = this.details?.locale?.getOrElse("en", {null})

    return DetailedMovieDomainModel(
        id = DEFAULT_ID,
        title = details?.movieTitle ?: page?.movieTitle ?: DEFAULT_TITLE,
        synopsis = details?.synopsis ?: DEFAULT_SYNOPSIS,
        pictureUrl = heros?.getOrElse("0", {null})?.imageUrl ?: DEFAULT_PICTURE_URL,
        runtime = parseRunTime(this.details?.runtime),
        releaseDate = parseDate(page?.releaseDate) ?: DEFAULT_RELEASE_DATE,
        trailerUrl = clips?.getOrElse(0) { null }?.versions?.enus?.sizes?.getOrElse("sd", { null})?.srcAlt ?: DEFAULT_TRAILER_URL
    )

}

fun parseDate(releaseDate: String?): Date? {
    return if(releaseDate.isNullOrEmpty()) {
        DEFAULT_RELEASE_DATE
    } else {
        try {
            SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(releaseDate)
        } catch (e: Exception) {
            DEFAULT_RELEASE_DATE
        }
    }
}

private fun parseRunTime (runTime: String?): Int {
    runTime?.let {
        val groupHours = "([0-9]+) ((hour)|(hours)|(h))".toRegex().find(runTime)
        val groupMinutes = "([0-9]+) ((minutes)|(minute)|(mns)|(m))".toRegex().find(runTime)

        val hour = groupHours?.groupValues?.get(1)?.toIntOrNull()
        val minutes = groupMinutes?.groupValues?.get(1)?.toIntOrNull()

        var timeInMinutes = 0
        if (hour != null)
            timeInMinutes = hour*60

        if (minutes != null)
            timeInMinutes += minutes

        if (timeInMinutes > 0)
            return timeInMinutes
    }

    return DEFAULT_RUNTIME
}