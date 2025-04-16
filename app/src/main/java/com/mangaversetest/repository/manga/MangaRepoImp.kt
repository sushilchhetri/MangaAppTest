package com.mangaversetest.repository.manga

import com.mangaversetest.domain.GetAllManageResponse
import com.mangaversetest.remote.MangaApiService
import io.reactivex.rxjava3.core.Single
import retrofit2.Response

class MangaRepoImp(private val apiService:MangaApiService) :MangaRepo {
    override fun getAllManga(pageNo: Int): Single<Response<GetAllManageResponse>> {
        return apiService.getAllManga(pageNo)
    }
}