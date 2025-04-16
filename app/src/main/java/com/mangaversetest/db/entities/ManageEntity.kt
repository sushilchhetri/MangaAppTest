package com.mangaversetest.db.entities


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "manage_table")
data class ManageEntity(
    @PrimaryKey val id: String,
    val authors: List<String>?,
    val genres:List<String>?,
    val nsfw: Boolean?,
    val status: String?,
    val subTitle: String?,
    val summary: String?,
    val thumb: String?,
    val title: String?,
    val totalChapter: Int?,
    val type: String?
)