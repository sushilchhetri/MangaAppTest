package com.mangaversetest.di


import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mangaversetest.db.MangaDatabase
import com.mangaversetest.remote.MangaApiService
import com.mangaversetest.repository.db.ManageDBRepo
import com.mangaversetest.repository.db.MangaDBRepoImp
import com.mangaversetest.repository.manga.MangaRepo
import com.mangaversetest.repository.manga.MangaRepoImp
import com.mangaversetest.utils.sharedPreferences.PreferenceManager
import com.mangaversetest.utils.sharedPreferences.UserPrefImp
import com.mangaversetest.utils.sharedPreferences.UserPrefRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class Modules {

    @Singleton
    @Provides
    fun providesRecordRepo(apiService: MangaApiService): MangaRepo {
        return MangaRepoImp(apiService)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MangaDatabase {
        return Room.databaseBuilder(
            context,
            MangaDatabase::class.java,
            "manage_database.db"
        ).setJournalMode(RoomDatabase.JournalMode.TRUNCATE) .build()
    }

    @Provides
    fun provideManageDao(database: MangaDatabase): ManageDBRepo {
        return MangaDBRepoImp(database.mangaDao())
    }

    @Provides
    @Singleton
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(PreferenceManager.PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun providePreferenceManager(preferences: SharedPreferences): PreferenceManager {
        return PreferenceManager(preferences, preferences.edit())
    }


    @Provides
    @Singleton
    fun userPre(preferenceManager: PreferenceManager): UserPrefRepo {
        return UserPrefImp(preferenceManager)
    }

}