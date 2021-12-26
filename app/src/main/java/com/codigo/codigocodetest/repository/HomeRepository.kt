package com.codigo.codigocodetest.repository

import android.content.Context
import com.codigo.codigocodetest.common.API_KEY
import com.codigo.codigocodetest.model.response.MovieDetailResponseModel
import com.codigo.codigocodetest.retrofit.APIService
import com.codigo.codigocodetest.roomdb.MovieDB
import com.codigo.codigocodetest.utility.showToast

class HomeRepository(
    private val context: Context,
    private val apiService: APIService
) {

    suspend fun getMovieDetail(movieId: Int): MovieDetailResponseModel? {
        var movieDetail: MovieDetailResponseModel? = null
        try {
            val response = apiService.getMovieDetail(movieId, API_KEY)
            if (response.isSuccessful)
                movieDetail = response.body()
        } catch (e: Exception) {
            context.showToast(e.localizedMessage)
        }
        return movieDetail
    }
}