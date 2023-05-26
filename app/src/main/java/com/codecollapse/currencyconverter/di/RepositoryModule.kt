package com.codecollapse.currencyconverter.di

import android.content.Context
import com.codecollapse.currencyconverter.data.repository.CountryRepository
import com.codecollapse.currencyconverter.data.repository.exchange.ExchangeRateRepositoryImpl
import com.codecollapse.currencyconverter.network.CurrencyApi
import com.codecollapse.currencyconverter.source.datastore.DataStoreRepository
import com.codecollapse.currencyconverter.source.local.dao.ExchangeRateDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideCountryRepository(): CountryRepository {
        return CountryRepository()
    }

    @Provides
    fun provideDataStoreRepository(@ApplicationContext context: Context) =
        DataStoreRepository(context)

    @Singleton
    @Provides
    fun provideExchangeRateRepository(
        currencyApi: CurrencyApi,
        exchangeRateDao: ExchangeRateDao
    ): ExchangeRateRepositoryImpl {
        return ExchangeRateRepositoryImpl(currencyApi = currencyApi, exchangeRateDao = exchangeRateDao)
    }
}