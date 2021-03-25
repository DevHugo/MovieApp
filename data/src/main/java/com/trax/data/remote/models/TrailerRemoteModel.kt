package com.trax.data.remote.models

import com.google.gson.annotations.SerializedName

data class TrailerRemoteModel (
    @SerializedName("src") val src: String?,
    @SerializedName("srcAlt") val srcAlt: String?)