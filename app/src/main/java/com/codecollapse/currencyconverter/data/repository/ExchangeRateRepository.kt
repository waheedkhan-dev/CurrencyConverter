package com.codecollapse.currencyconverter.data.repository

import com.codecollapse.currencyconverter.data.model.exchangeRate.ExchangeRate
import com.codecollapse.currencyconverter.network.CurrencyApi
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ExchangeRateRepository @Inject constructor(private val currencyApi: CurrencyApi) {

    fun getLatestExchangeRates(api_key: String, baseCurrency: String): Flow<ExchangeRate> = flow {
        val response =
            currencyApi.getLatestExchangeRates(api_key = api_key, baseCurrency = baseCurrency)
        if (response.isSuccessful) {
            emit(response.body()!!)
        }
    }.flowOn(IO)
}