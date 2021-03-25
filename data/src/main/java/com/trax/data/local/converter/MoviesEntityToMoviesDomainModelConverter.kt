package com.trax.data.local.converter

import com.trax.data.local.MovieEntity
import com.trax.models.DetailedMovieDomainModel

fun List<DetailedMovieDomainModel>.toEntity() =
    map { it.toEntity() }

fun DetailedMovieDomainModel.toEntity() = MovieEntity(
    id = this.id,
    title = this.title,
    synopsis = this.synopsis,
    pictureUrl = this.pictureUrl,
    runtime = this.runtime,
    releaseDate = this.releaseDate,
    trailerUrl = this.trailerUrl
)