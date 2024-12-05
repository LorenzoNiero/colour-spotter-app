package com.challenge.colour_spotter.domain.di

import com.challenge.colour_spotter.domain.repository.ColorRepository
import com.challenge.colour_spotter.domain.repository.ColorRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal class DomainModule {
    @Provides
    @Singleton
    fun provideHomeRepository(repository: ColorRepositoryImpl): ColorRepository = repository

}