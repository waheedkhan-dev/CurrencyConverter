package com.codecollapse.currencyconverter.data.repository.exchange

import com.codecollapse.currencyconverter.data.model.exchangeRate.ExchangeRate
import kotlinx.coroutines.flow.Flow

interface ExchangeRateRepository {

    fun getLatestExchangeRates(api_key: String, baseCurrency: String,symbols : String = ""): Flow<ExchangeRate>
    fun updateBaseCurrency(api_key: String,baseCurrency: String,symbols: String) : Flow<Unit>

}