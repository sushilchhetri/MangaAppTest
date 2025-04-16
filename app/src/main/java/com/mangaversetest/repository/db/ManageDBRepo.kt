package com.mangaversetest.repository.db

import com.mangaversetest.db.entities.ManageEntity
import com.mangaversetest.domain.ManageModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable


interface ManageDBRepo {
    fun insertAll(manageList: List<ManageModel>): Completable
    fun getAll(): Flowable<List<ManageEntity>>
}