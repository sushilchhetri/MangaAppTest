package com.mangaversetest.repository.db

import android.util.Log
import com.mangaversetest.db.dao.MangaDao
import com.mangaversetest.db.entities.ManageEntity
import com.mangaversetest.domain.ManageModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

import java.util.UUID
import javax.inject.Inject

class MangaDBRepoImp @Inject constructor(private val dbDao: MangaDao):ManageDBRepo {

    override fun insertAll(manageList: List<ManageModel>): Completable {
        val entities = manageList.map {
            ManageEntity(
                id = it.id ?: UUID.randomUUID().toString(),
                authors = it.authors,
                genres = it.genres,
                nsfw = it.nsfw,
                status = it.status,
                subTitle = it.subTitle,
                summary = it.summary,
                thumb = it.thumb,
                title = it.title,
                totalChapter = it.totalChapter,
                type = it.type
            )
        }
        return dbDao.insertAll(entities)
    }

    override fun getAll(): Flowable<List<ManageEntity>> {
        return  dbDao.getAll()
    }
}