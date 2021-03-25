package com.trax.data.local.converter

import com.trax.data.local.MovieEntity
import com.trax.models.DetailedMovieDomainModel

fun List<MovieEntity>.toDomainModel() =
    map { it.toDomainModel() }

fun MovieEntity.toDomainModel() = DetailedMovieDomainModel(
    id = this.id,
    title = this.title,
    synopsis = this.synopsis,
    pictureUrl = this.pictureUrl,
    runtime = this.runtime,
    releaseDate = this.releaseDate,
    trailerUrl = this.trailerUrl
)