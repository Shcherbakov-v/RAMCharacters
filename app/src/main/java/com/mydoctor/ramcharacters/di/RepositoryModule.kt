package com.mydoctor.ramcharacters.di

import com.mydoctor.ramcharacters.data.database.CharacterDao
import com.mydoctor.ramcharacters.data.database.CharacterRemoteKeysDao
import com.mydoctor.ramcharacters.data.database.RAMDatabase
import com.mydoctor.ramcharacters.data.repository.CharacterRemoteMediator
import com.mydoctor.ramcharacters.data.repository.CharacterRepository
import com.mydoctor.ramcharacters.network.RAMApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideCharacterRemoteKeysDao(
        database: RAMDatabase
    ): CharacterRemoteKeysDao {
        return database.characterRemoteKeysDao()
    }

    @Provides
    fun provideCharacterRemoteMediator(
        ramApiService: RAMApiService,
        characterDatabase: RAMDatabase,
        characterDao: CharacterDao,
        characterRemoteKeysDao: CharacterRemoteKeysDao
    ): CharacterRemoteMediator {
        return CharacterRemoteMediator(
            ramApiService,
            characterDatabase,
            characterDao,
            characterRemoteKeysDao
        )
    }

    @Provides
    fun provideCharacterRepository(
        characterRemoteMediator: CharacterRemoteMediator,
        characterDao: CharacterDao,
    ): CharacterRepository {
        return CharacterRepository(
            characterDao = characterDao,
            characterRemoteMediator = characterRemoteMediator,
        )
    }
}
