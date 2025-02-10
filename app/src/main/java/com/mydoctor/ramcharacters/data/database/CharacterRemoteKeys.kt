package com.mydoctor.ramcharacters.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "character_remote_keys")
data class CharacterRemoteKeys(
    @PrimaryKey val page: Int,
    val prevKey: Int?,
    val nextKey: Int?
)