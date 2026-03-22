package com.akas62083.qm.di

import android.content.Context
import androidx.room.Room
import com.akas62083.qm.db.AppDatabase
import com.akas62083.qm.db.MapDao
import com.akas62083.qm.repository.data_repo.MapDataRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()

    @Provides
    fun provideDao(database: AppDatabase): MapDao = database.getDao()

}