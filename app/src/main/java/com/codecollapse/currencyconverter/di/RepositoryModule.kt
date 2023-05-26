package com.codecollapse.currencyconverter.di

import com.codecollapse.currencyconverter.data.repository.CountryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideCountryRepository(): CountryRepository {
        return CountryRepository()
    }
}