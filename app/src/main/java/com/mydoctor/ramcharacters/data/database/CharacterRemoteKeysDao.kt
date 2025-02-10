package com.mydoctor.ramcharacters.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CharacterRemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(remoteKey: CharacterRemoteKeys)

    @Query("SELECT * FROM character_remote_keys ORDER BY page DESC LIMIT 1")
    suspend fun getRemoteKeyForLastItem(): CharacterRemoteKeys?

    @Query("DELETE FROM character_remote_keys")
    suspend fun clearRemoteKeys()
}