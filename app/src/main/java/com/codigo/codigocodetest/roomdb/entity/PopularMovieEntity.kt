package com.codigo.codigocodetest.roomdb.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_popular_movie")
data class PopularMovieEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "backdrop_path") val backDropPath: String?,
    @ColumnInfo(name = "original_language") val originalLanguage: String?,
    @ColumnInfo(name = "overview") val overview: String?,
    @ColumnInfo(name = "popularity") val popularity: Double?,
    @ColumnInfo(name = "poster_path") val poster_path: String?,
    @ColumnInfo(name = "release_date") val releaseDate: String?,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "vote_average") val vote_average: Double?,
    @ColumnInfo(name = "fav") val favorite: Int = 0,
    )