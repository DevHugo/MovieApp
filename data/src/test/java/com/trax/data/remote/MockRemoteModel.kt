package com.trax.data.remote

import com.trax.data.remote.models.*

val moviesRemoteModel = MoviesRemoteModel(
    page = PageRemoteModel("The Most Fabulous Year", "2019-04-12"),
    details = DetailsRemoteModel(locale =
                mutableMapOf(
                    "en" to DetailedMovieRemoteModel("The Most Dangerous Year", "In 2016 a small group of families with transgender kids joined the fight against a wave of discriminatory anti-transgender legislation"),
                    "fr" to DetailedMovieRemoteModel("Année la plus dangereuse", "Un film ...")), "1 hour 30 minutes"),
    heros = mutableMapOf("0" to HerosImageUrlRemoteModel("http://trailers.apple.com/trailers/independent/the-most-dangerous-year/images/background.jpg")),
    clips = listOf(TrailersRemoteModel(TrailersEnusRemoteModel(TrailersSizeRemoteModel(mutableMapOf("hd720" to TrailerRemoteModel("https://movietrailers.ap…-year-trailer-1_720p.mov", "https://movietrailers.ap…year-trailer-1_a720p.m4v"))))))
)