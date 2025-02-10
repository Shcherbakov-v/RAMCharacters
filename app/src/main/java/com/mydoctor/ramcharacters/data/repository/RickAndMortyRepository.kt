package com.mydoctor.ramcharacters.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.mydoctor.ramcharacters.data.database.CharacterDao
import com.mydoctor.ramcharacters.model.Character
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    private val characterDao: CharacterDao,
    private val characterRemoteMediator: CharacterRemoteMediator,
) {
    @OptIn(ExperimentalPagingApi::class)
    fun getCharacters(): Flow<PagingData<Character>> {
        val pagingSourceFactory = {
            characterDao.getAllCharacters()
        }

        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 2,
                maxSize = PagingConfig.MAX_SIZE_UNBOUNDED,
                jumpThreshold = Int.MIN_VALUE,
                enablePlaceholders = true,
            ),
            remoteMediator = characterRemoteMediator,
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
}