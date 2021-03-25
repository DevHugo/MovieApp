package com.trax.models

import java.util.*

data class DetailedMovieDomainModel (
    val id: Int,
    val title: String,
    val synopsis: String,
    val pictureUrl: String,
    val runtime: Int,
    val releaseDate: Date,
    val trailerUrl: String
) {
    companion object {
        const val DEFAULT_ID = 0
        const val DEFAULT_TITLE = ""
        const val DEFAULT_SYNOPSIS = ""
        const val DEFAULT_PICTURE_URL = ""
        const val DEFAULT_RUNTIME = 0
        val DEFAULT_RELEASE_DATE = Date(0)
        const val DEFAULT_TRAILER_URL = ""

    }
}