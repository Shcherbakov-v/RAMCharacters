package com.mydoctor.ramcharacters.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.mydoctor.ramcharacters.data.database.CharacterDao
import com.mydoctor.ramcharacters.data.database.CharacterRemoteKeys
import com.mydoctor.ramcharacters.data.database.CharacterRemoteKeysDao
import com.mydoctor.ramcharacters.data.database.RAMDatabase
import com.mydoctor.ramcharacters.model.Character
import com.mydoctor.ramcharacters.model.CharacterRemote
import com.mydoctor.ramcharacters.model.getNextPage
import com.mydoctor.ramcharacters.model.getPrevPage
import com.mydoctor.ramcharacters.network.RAMApiService
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class CharacterRemoteMediator(
    private val ramApiService: RAMApiService,
    private val characterDatabase: RAMDatabase,
    private val characterDao: CharacterDao,
    private val characterRemoteKeysDao: CharacterRemoteKeysDao
) : RemoteMediator<Int, Character>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Character>
    ): MediatorResult {
        return try {
            val page: Int = when (loadType) {
                LoadType.REFRESH -> START_PAGE
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKey = characterDatabase.withTransaction {
                        characterRemoteKeysDao.getRemoteKeyForLastItem()
                    }

                    if (remoteKey?.nextKey == null) {
                        return MediatorResult.Success(endOfPaginationReached = false)
                    } else {
                        remoteKey.nextKey
                    }
                }
            }

            val response = ramApiService.getAllCharacters(page)
            val prevPage: Int? = response.info.getPrevPage()
            val nextPage: Int? = response.info.getNextPage()

            characterDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    characterRemoteKeysDao.clearRemoteKeys()
                    characterDao.clearAllCharacters()
                }
                characterRemoteKeysDao.insertOrReplace(
                    CharacterRemoteKeys(page, prevPage, nextPage)
                )
                characterDao.insertAll(response.results.map { it.toCharacter() })
            }

            return MediatorResult.Success(
                endOfPaginationReached = nextPage == null
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    companion object {
        private const val START_PAGE = 1
    }

    private fun CharacterRemote.toCharacter(): Character {
        return Character(
            id = id,
            name = name,
            status = status,
            species = species,
            gender = gender,
            image = image,
            locationName = location.name,
            locationUrl = location.url,
            episode = episode
        )
    }
}