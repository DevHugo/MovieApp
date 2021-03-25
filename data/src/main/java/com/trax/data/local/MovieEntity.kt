package com.trax.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class MovieEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val synopsis: String,
    val pictureUrl: String,
    val runtime: Int,
    val releaseDate: Date,
    val trailerUrl: String
)