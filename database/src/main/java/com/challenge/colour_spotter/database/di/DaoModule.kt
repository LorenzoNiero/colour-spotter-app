package com.challenge.colour_spotter.database.di

import com.challenge.colour_spotter.database.AppDatabase
import com.challenge.colour_spotter.database.dao.ColorDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    fun providesColorDao(
        database: AppDatabase
    ): ColorDao = database.colorDao()

}