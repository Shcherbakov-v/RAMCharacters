package com.mydoctor.ramcharacters.network

import com.mydoctor.ramcharacters.model.CharacterResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface RAMApiService {

    @GET("character")
    suspend fun getAllCharacters(@Query("page") page: Int): CharacterResponse
}