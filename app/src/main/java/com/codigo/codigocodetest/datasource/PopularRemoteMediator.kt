package com.codigo.codigocodetest.datasource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.codigo.codigocodetest.common.API_KEY
import com.codigo.codigocodetest.retrofit.APIService
import com.codigo.codigocodetest.roomdb.MovieDB
import com.codigo.codigocodetest.roomdb.entity.PopularMovieEntity
import com.codigo.codigocodetest.roomdb.entity.RemoteKeyEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PopularRemoteMediator(
    private val api: APIService,
    private val db: MovieDB
) : RemoteMediator<Int, PopularMovieEntity>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, PopularMovieEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                if (prevKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                if (nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                nextKey
            }
        }

        try {
            val apiResponse = api.getPopularMovies(API_KEY, page)
            apiResponse.body()
            val results = apiResponse.body()!!.results
            val endOfPaginationReached = results.isEmpty()
            withContext(Dispatchers.IO) {
                db.withTransaction {
                    // clear all tables in the database
                    if (loadType == LoadType.REFRESH) {
                        db.remoteKeyDao().popularDeleteAll()
                        db.movieDao().deleteAllPopular()
                    }
                    val prevKey = if (page == 1) null else page - 1
                    val nextKey = if (endOfPaginationReached) null else page + 1
                    val keys = results.map {
                        RemoteKeyEntity(movieId = it.id, prevKey = prevKey, nextKey = nextKey)
                    }
                    db.remoteKeyDao().popularInsertAll(keys)
                    db.movieDao().insertAllPopular(results)
                }
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, PopularMovieEntity>
    ): RemoteKeyEntity? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { movieId ->
                db.remoteKeyDao().popularRemotes(movieId)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, PopularMovieEntity>): RemoteKeyEntity? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { repo ->
                // Get the remote keys of the first items retrieved
                db.remoteKeyDao().popularRemotes(repo.id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, PopularMovieEntity>): RemoteKeyEntity? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { repo ->
                // Get the remote keys of the last item retrieved
                db.remoteKeyDao().popularRemotes(repo.id)
            }
    }
}