package com.mangaversetest.remote

import com.mangaversetest.domain.GetAllManageResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MangaApiService {
    @GET("manga/fetch")
    fun getAllManga(@Query("page") pageNo:Int ):Single<Response<GetAllManageResponse>>
}