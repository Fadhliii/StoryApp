package com.example.gagalmuluyaallah.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeysDao {
    // ! Insert all remote keys
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeys>)

    // ! Get remote keys by id
    @Query("SELECT * FROM remote_keys WHERE id = :id")
    suspend fun getRemoteKeysId(id: String): RemoteKeys?

    //! Delete all remote keys
    @Query("DELETE FROM remote_keys")
    suspend fun deleteRemoteKeys()


}