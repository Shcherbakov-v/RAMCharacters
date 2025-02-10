package com.mydoctor.ramcharacters.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "characters")
data class Character(
    @PrimaryKey
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val image: String,
    @ColumnInfo(name = "location_name")
    val locationName: String = "",
    @ColumnInfo(name = "location_url")
    val locationUrl: String = "",
    @ColumnInfo(defaultValue = "")
    val episode: List<String> = emptyList(),
)