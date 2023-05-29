package com.codecollapse.currencyconverter.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.codecollapse.currencyconverter.data.repository.CommonCurrencyRepository
import com.codecollapse.currencyconverter.data.repository.datastore.DataStoreRepositoryImpl
import com.codecollapse.currencyconverter.data.repository.exchange.ExchangeRateRepositoryImpl
import com.codecollapse.currencyconverter.data.repository.rate.RateConverterRepositoryImpl
import com.codecollapse.currencyconverter.network.CurrencyApi
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
    fun provideCountryRepository(@ApplicationContext context : Context): CommonCurrencyRepository {
        return CommonCurrencyRepository(context)
    }

    @Singleton
    @Provides
    fun provideDataStoreRepository(dataStore: DataStore<Preferences>): DataStoreRepositoryImpl {
        return DataStoreRepositoryImpl(ccDataStore = dataStore)
    }

    @Singleton
    @Provides
    fun provideExchangeRateRepository(
        currencyApi: CurrencyApi,
        exchangeRateDao: ExchangeRateDao
    ): ExchangeRateRepositoryImpl {
        return ExchangeRateRepositoryImpl(
            currencyApi = currencyApi,
            exchangeRateDao = exchangeRateDao
        )
    }

    @Singleton
    @Provides
    fun provideRateConverterRepository(
        currencyApi: CurrencyApi
    ): RateConverterRepositoryImpl {
        return RateConverterRepositoryImpl(currencyApi)
    }
}