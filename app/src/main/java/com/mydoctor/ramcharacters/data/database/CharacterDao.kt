package com.mydoctor.ramcharacters.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.mydoctor.ramcharacters.model.Character

@Dao
interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<Character>)

    @Query("SELECT * FROM characters ORDER BY id ASC")
    fun getAllCharacters(): PagingSource<Int, Character>

    @Transaction
    @Query("DELETE FROM characters")
    suspend fun clearAllCharacters()
}