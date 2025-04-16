package com.mangaversetest.domain

import com.google.gson.annotations.SerializedName


data class GetAllManageResponse(
    val code: Int? = null,
    @SerializedName("data")
    val data: List<ManageModel>? = null
)

data class ManageModel(
    val authors: List<String>? = null,
    val genres: List<String>? = null,
    val id: String? = null,
    val nsfw: Boolean? = null,
    val status: String? = null,
    @SerializedName("sub_title")
    val subTitle: String? = null,
    val summary: String? = null,
    val thumb: String? = null,
    val title: String? = null,
    @SerializedName("total_chapter")
    val totalChapter: Int? = null,
    val type: String? = null,
)