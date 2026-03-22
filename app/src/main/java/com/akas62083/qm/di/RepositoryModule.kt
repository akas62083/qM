package com.akas62083.qm.di

import com.akas62083.qm.repository.data_repo.MapDataRepository
import com.akas62083.qm.repository.data_repo.impl.MapDataRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindRepository(
        mapDataRepositoryImpl: MapDataRepositoryImpl
    ): MapDataRepository
}
