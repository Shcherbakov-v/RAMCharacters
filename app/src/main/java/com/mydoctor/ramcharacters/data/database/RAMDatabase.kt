package com.mydoctor.ramcharacters.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mydoctor.ramcharacters.model.Character

@Database(
    entities = [Character::class, CharacterRemoteKeys::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class RAMDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
    abstract fun characterRemoteKeysDao(): CharacterRemoteKeysDao

}