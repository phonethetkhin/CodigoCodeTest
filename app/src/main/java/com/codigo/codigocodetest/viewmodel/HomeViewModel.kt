package com.codigo.codigocodetest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.codigo.codigocodetest.datasource.PopularRemoteMediator
import com.codigo.codigocodetest.datasource.UpcomingRemoteMediator
import com.codigo.codigocodetest.model.response.MovieDetailResponseModel
import com.codigo.codigocodetest.repository.HomeRepository
import com.codigo.codigocodetest.retrofit.APIService
import com.codigo.codigocodetest.roomdb.MovieDB
import kotlinx.coroutines.launch

class HomeViewModel(
    val repository: HomeRepository,
    val apiService: APIService,
    val movieDb: MovieDB
) : ViewModel() {
    //tabControlVariable
    lateinit var tabPositionLiveData: MutableLiveData<Int>
    lateinit var movieDetailLiveData: MutableLiveData<MovieDetailResponseModel?>

    //tabPositionControl--------------------------------------------------------------------------//
    fun getTabPositionLiveData(): LiveData<Int> {
        if (!::tabPositionLiveData.isInitialized) {
            tabPositionLiveData = MutableLiveData(0)
        }
        return tabPositionLiveData
    }

    fun setTabPositionLiveData(value: Int) {
        if (::tabPositionLiveData.isInitialized) {
            tabPositionLiveData.postValue(value)
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getUpComing() = Pager(
        PagingConfig(pageSize = 20, enablePlaceholders = false),
        remoteMediator = UpcomingRemoteMediator(apiService, movieDb)
    ) {
        movieDb.movieDao().getAllUpcoming()
    }.flow.cachedIn(viewModelScope)

    @OptIn(ExperimentalPagingApi::class)
    fun getPopular() = Pager(
        PagingConfig(pageSize = 20, enablePlaceholders = false),
        remoteMediator = PopularRemoteMediator(apiService, movieDb)
    ) {
        movieDb.movieDao().getAllPopular()
    }.flow.cachedIn(viewModelScope)

    fun getMovieDetailLiveData(movieId: Int): LiveData<MovieDetailResponseModel?> {
        if (!::movieDetailLiveData.isInitialized) {
            movieDetailLiveData = MutableLiveData()
            getMovieDetail(movieId)
        }
        return movieDetailLiveData
    }

    fun getMovieDetail(movieId: Int) = viewModelScope.launch {
        movieDetailLiveData.postValue(repository.getMovieDetail(movieId))
    }

}
