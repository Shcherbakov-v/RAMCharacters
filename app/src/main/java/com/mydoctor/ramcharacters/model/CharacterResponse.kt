package com.mydoctor.ramcharacters.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class CharacterResponse(
    val info: Info,
    val results: List<CharacterRemote>
)

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class Info(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class CharacterRemote(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val location: Location,
    val image: String,
    val episode: List<String>,
)

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class Location(
    val name: String,
    val url: String,
)

fun Info.getNextPage(): Int? {
    return getPageNumber(next)
}

fun Info.getPrevPage(): Int? {
    return getPageNumber(prev)
}

private fun getPageNumber(pageParameter: String?) : Int? {
    return pageParameter?.substringAfter("page=")?.trim()?.toIntOrNull()
}