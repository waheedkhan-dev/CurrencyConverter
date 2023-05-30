package com.codecollapse.currencyconverter.data.repository.exchange

import com.codecollapse.currencyconverter.data.model.exchangeRate.ExchangeRate
import com.codecollapse.currencyconverter.network.CurrencyApi
import com.codecollapse.currencyconverter.source.local.dao.ExchangeRateDao
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ExchangeRateRepositoryImpl @Inject constructor(
    private val currencyApi: CurrencyApi,
    private val exchangeRateDao: ExchangeRateDao
) : ExchangeRateRepository {
    override fun getLatestExchangeRates(
        api_key: String,
        baseCurrency: String,
        symbols: String
    ): Flow<ExchangeRate> {
        return flow {
            val response =
                currencyApi.getLatestExchangeRates(
                    api_key = api_key,
                    baseCurrency = baseCurrency,
                    symbols = symbols
                )
            if (response.isSuccessful) {
                val exchangeRate = response.body()
                exchangeRateDao.insertLatestExchangeRates(exchangeRate = exchangeRate!!)
                emit(exchangeRateDao.getLatestExchangeRatesFromDb())
            }
        }.flowOn(IO)
    }
}