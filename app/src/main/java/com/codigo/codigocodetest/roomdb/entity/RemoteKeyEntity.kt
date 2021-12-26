package com.codigo.codigocodetest.roomdb.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_remote_keys")
data class RemoteKeyEntity(
    @PrimaryKey val movieId: Int,
    val prevKey: Int?,
    val nextKey: Int?
)