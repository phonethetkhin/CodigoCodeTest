package com.codigo.codigocodetest.roomdb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.codigo.codigocodetest.roomdb.entity.RemoteKeyEntity

@Dao
interface RemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upComingInsertAll(remoteKey: List<RemoteKeyEntity>)

    @Query("SELECT * FROM tbl_remote_keys WHERE movieId = :id")
    suspend fun upComingRemotes(id: Int): RemoteKeyEntity?

    @Query("DELETE FROM tbl_remote_keys")
    suspend fun upComingDeleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun popularInsertAll(remoteKey: List<RemoteKeyEntity>)

    @Query("SELECT * FROM tbl_remote_keys WHERE movieId = :id")
    suspend fun popularRemotes(id: Int): RemoteKeyEntity?

    @Query("DELETE FROM tbl_remote_keys")
    suspend fun popularDeleteAll()


}