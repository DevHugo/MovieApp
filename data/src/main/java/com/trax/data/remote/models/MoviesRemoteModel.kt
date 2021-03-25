package com.trax.data.remote.models

import com.google.gson.annotations.SerializedName

data class MoviesRemoteModel (
    @SerializedName("page") val page: PageRemoteModel?,
    @SerializedName("details") val details: DetailsRemoteModel?,
    @SerializedName( "heros") val heros: Map<String, HerosImageUrlRemoteModel>?,
    @SerializedName( "clips") val clips: List<TrailersRemoteModel>?,
)