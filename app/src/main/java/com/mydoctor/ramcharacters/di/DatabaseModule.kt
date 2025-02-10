package com.mydoctor.ramcharacters.di

import android.content.Context
import androidx.room.Room
import com.mydoctor.ramcharacters.data.database.CharacterDao
import com.mydoctor.ramcharacters.data.database.Converters
import com.mydoctor.ramcharacters.data.database.RAMDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    fun provideConverters(): Converters = Converters()

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext appContext: Context,
        converters: Converters
    ): RAMDatabase {
        return Room.databaseBuilder(
            appContext,
            RAMDatabase::class.java,
            "ram.db"
        )
            .addTypeConverter(converters)
            .build()
    }

    @Provides
    fun provideCharacterDao(database: RAMDatabase): CharacterDao {
        return database.characterDao()
    }
}
