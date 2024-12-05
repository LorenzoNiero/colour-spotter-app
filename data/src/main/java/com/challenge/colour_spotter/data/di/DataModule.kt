package com.challenge.colour_spotter.data.di

import com.challenge.colour_spotter.data.datasource.ColorLocalDataSource
import com.challenge.colour_spotter.data.datasource.ColorLocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal class DataModule {

    @Provides
    @Singleton
    fun provideBreedLocalDataSource(localDataSourceImpl: ColorLocalDataSourceImpl): ColorLocalDataSource = localDataSourceImpl
}