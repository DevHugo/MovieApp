package com.trax.data.remote.models

import com.google.gson.annotations.SerializedName

data class HerosImageUrlRemoteModel (
    @SerializedName("imageurl") val imageUrl: String?
)