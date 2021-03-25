package com.trax.data.remote.models

import com.google.gson.annotations.SerializedName
import java.util.*


data class PageRemoteModel (
    @SerializedName( "movie_title") val movieTitle: String?,
    @SerializedName( "release_date") val releaseDate: String?,
)