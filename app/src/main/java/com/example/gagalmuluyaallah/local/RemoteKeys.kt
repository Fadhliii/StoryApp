package com.example.gagalmuluyaallah.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
        //! this one is the primary key of the table remote_keys
        @PrimaryKey val id: String,
        val prevKey: Int?,
        val nextKey: Int?

)