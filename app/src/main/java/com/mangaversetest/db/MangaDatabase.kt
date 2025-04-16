package com.mangaversetest.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mangaversetest.db.dao.MangaDao
import com.mangaversetest.db.entities.ManageEntity
import com.mangaversetest.db.utils.Converters

@Database(
    entities = [
        ManageEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MangaDatabase: RoomDatabase() {
    abstract fun mangaDao(): MangaDao

}