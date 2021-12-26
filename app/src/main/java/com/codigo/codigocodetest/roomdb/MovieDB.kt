package com.codigo.codigocodetest.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.codigo.codigocodetest.roomdb.dao.MovieDao
import com.codigo.codigocodetest.roomdb.dao.RemoteKeyDao
import com.codigo.codigocodetest.roomdb.entity.PopularMovieEntity
import com.codigo.codigocodetest.roomdb.entity.RemoteKeyEntity
import com.codigo.codigocodetest.roomdb.entity.UpComingMovieEntity

@Database(
    entities = [UpComingMovieEntity::class, RemoteKeyEntity::class, PopularMovieEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MovieDB : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun remoteKeyDao(): RemoteKeyDao

    companion object {
        @Volatile
        private var INSTANCE: MovieDB? = null
        fun getInstance(context: Context): MovieDB =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                MovieDB::class.java, "CodigoMovie.db"
            ).allowMainThreadQueries()
                .build()
    }
}