package com.codigo.codigocodetest.roomdb.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.codigo.codigocodetest.roomdb.entity.PopularMovieEntity
import com.codigo.codigocodetest.roomdb.entity.UpComingMovieEntity

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllUpComing(upComingMovies: List<UpComingMovieEntity>)

    @Query("SELECT * FROM tbl_upcoming_movie ORDER BY id")
    fun getAllUpcoming(): PagingSource<Int, UpComingMovieEntity>

    @Query("DELETE FROM tbl_upcoming_movie")
    suspend fun deleteAllUpComing()


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllPopular(popularMovieEntity: List<PopularMovieEntity>)

    @Query("SELECT * FROM tbl_popular_movie ORDER BY id")
    fun getAllPopular(): PagingSource<Int, PopularMovieEntity>

    @Query("DELETE FROM tbl_popular_movie")
    suspend fun deleteAllPopular()


    @Query("UPDATE tbl_upcoming_movie SET fav=1 WHERE id=:id ")
    fun setFavoriteUpComing(id: Int)

    @Query("SELECT fav FROM tbl_upcoming_movie WHERE id=:id")
    fun getFavStatusUpComing(id: Int): Int

    @Query("UPDATE tbl_upcoming_movie SET fav=0 WHERE id=:id ")
    fun removeFavoriteUpComing(id: Int)


    @Query("UPDATE tbl_popular_movie SET fav=1 WHERE id=:id ")
    fun setFavoritePopular(id: Int)

    @Query("SELECT fav FROM tbl_popular_movie WHERE id=:id")
    fun getFavStatusPopular(id: Int): Int

    @Query("UPDATE tbl_popular_movie SET fav=0 WHERE id=:id ")
    fun removeFavoritePopular(id: Int)
}