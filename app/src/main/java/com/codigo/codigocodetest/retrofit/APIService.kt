package com.codigo.codigocodetest.retrofit

import com.codigo.codigocodetest.model.response.MovieDetailResponseModel
import com.codigo.codigocodetest.model.response.PopularMovieResponseModel
import com.codigo.codigocodetest.model.response.UpComingMovieResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface APIService {
    @GET("/3/movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): Response<UpComingMovieResponseModel>

    @GET("/3/movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): Response<PopularMovieResponseModel>

    @GET("/3/movie/{id}")
    suspend fun getMovieDetail(
        @Path("id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Response<MovieDetailResponseModel>
}
