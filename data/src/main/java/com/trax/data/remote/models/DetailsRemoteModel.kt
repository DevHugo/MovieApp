package com.trax.data.remote.models

import com.google.gson.annotations.SerializedName

data class DetailsRemoteModel (
    @SerializedName("locale") val locale: Map<String, DetailedMovieRemoteModel>?,
    @SerializedName("run_time") val runtime: String?,
)