package com.trax.data.remote.models

import com.google.gson.annotations.SerializedName

data class DetailedMovieRemoteModel (
    @SerializedName("movie_title") val movieTitle: String?,
    @SerializedName("synopsis") val synopsis: String?,
)