package com.mangaversetest.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mangaversetest.db.entities.ManageEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface MangaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(manageList: List<ManageEntity>): Completable

    @Query("SELECT * FROM manage_table")
    fun getAll(): Flowable<List<ManageEntity>>

}