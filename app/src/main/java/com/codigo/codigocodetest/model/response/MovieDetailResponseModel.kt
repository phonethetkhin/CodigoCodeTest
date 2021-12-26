package com.codigo.codigocodetest.model.response

data class MovieDetailResponseModel(
    val backdrop_path: String,
    val genres: List<Genre>,
    val id: Int,
    val imdb_id: String,
    val original_language: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val runtime: Int,
    val status: String,
    val tagline: String,
    val title: String,
    val vote_average: Double,
)

data class Genre(
    val id: Int,
    val name: String
)
