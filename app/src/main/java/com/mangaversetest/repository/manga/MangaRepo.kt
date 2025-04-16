package com.mangaversetest.repository.manga

import com.mangaversetest.domain.GetAllManageResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.Query

interface MangaRepo {
    fun getAllManga( pageNo:Int ):Single<Response<GetAllManageResponse>>
}