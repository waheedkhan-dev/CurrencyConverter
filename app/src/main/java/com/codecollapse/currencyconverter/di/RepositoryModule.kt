package com.codecollapse.currencyconverter.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.codecollapse.currencyconverter.data.repository.CommonCurrencyRepository
import com.codecollapse.currencyconverter.data.repository.datastore.DataStoreRepositoryImpl
import com.codecollapse.currencyconverter.data.repository.exchange.ExchangeRateRepositoryImpl
import com.codecollapse.currencyconverter.data.repository.rate.RateConverterRepositoryImpl
import com.codecollapse.currencyconverter.network.CurrencyApi
import com.codecollapse.currencyconverter.source.local.dao.CurrencyDao
import com.codecollapse.currencyconverter.source.local.dao.ExchangeRateDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideCountryRepository(
        @ApplicationContext context: Context,
        currencyDao: CurrencyDao,
        exchangeRateRepositoryImpl: ExchangeRateRepositoryImpl,
        dataStoreRepositoryImpl: DataStoreRepositoryImpl,
        currencyApi: CurrencyApi,
        exchangeRateDao: ExchangeRateDao
    ): CommonCurrencyRepository {
        return CommonCurrencyRepository(
            context,
            currencyDao = currencyDao,
            exchangeRateRepositoryImpl = exchangeRateRepositoryImpl,
            dataStoreRepositoryImpl = dataStoreRepositoryImpl,
            currencyApi = currencyApi,
            exchangeRateDao = exchangeRateDao
        )
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
        currencyDao: CurrencyDao,
        exchangeRateDao: ExchangeRateDao,
        dataStoreRepositoryImpl: DataStoreRepositoryImpl,
        coroutineScope: CoroutineScope
    ): ExchangeRateRepositoryImpl {
        return ExchangeRateRepositoryImpl(
            currencyApi = currencyApi,
            exchangeRateDao = exchangeRateDao,
            currencyDao = currencyDao,
            dataStoreRepositoryImpl = dataStoreRepositoryImpl,
            coroutineScope = coroutineScope
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