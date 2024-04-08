package com.dhxxn17.data.di

import com.dhxxn17.data.datasource.SwapRemoteDataSource
import com.dhxxn17.data.datasource.SwapRemoteDataSourceImpl
import com.dhxxn17.data.repository.SwapRepositoryImpl
import com.dhxxn17.domain.repository.SwapRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindSwapDataSource(
        source: SwapRemoteDataSourceImpl
    ): SwapRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindSwapRepository(
        repository: SwapRepositoryImpl
    ): SwapRepository
}