package com.codigo.codigocodetest.model.response

import com.codigo.codigocodetest.roomdb.entity.PopularMovieEntity
import com.codigo.codigocodetest.roomdb.entity.UpComingMovieEntity

data class UpComingMovieResponseModel(
    val results: List<UpComingMovieEntity>,
    val total_pages: Int,
)

data class PopularMovieResponseModel(
    val results: List<PopularMovieEntity>,
    val total_pages: Int,
)
