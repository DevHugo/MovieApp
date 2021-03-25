package com.trax.data.remote.models

import com.google.gson.annotations.SerializedName

data class TrailersEnusRemoteModel (
    @SerializedName("enus") val enus: TrailersSizeRemoteModel
        )

data class TrailersSizeRemoteModel (
    @SerializedName("sizes") val sizes: Map<String, TrailerRemoteModel>?
)