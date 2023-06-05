package com.codecollapse.currencyconverter.data.repository.exchange

import com.codecollapse.currencyconverter.data.model.currency.Currency
import com.codecollapse.currencyconverter.data.model.exchangeRate.ExchangeRate
import com.codecollapse.currencyconverter.data.repository.datastore.DataStoreRepositoryImpl
import com.codecollapse.currencyconverter.network.CurrencyApi
import com.codecollapse.currencyconverter.source.local.dao.CurrencyDao
import com.codecollapse.currencyconverter.source.local.dao.ExchangeRateDao
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ExchangeRateRepositoryImpl @Inject constructor(
    private val currencyApi: CurrencyApi,
    private val currencyDao: CurrencyDao,
    private val exchangeRateDao: ExchangeRateDao,
    private val dataStoreRepositoryImpl: DataStoreRepositoryImpl
) : ExchangeRateRepository {
    override fun getLatestExchangeRates(
        api_key: String,
        baseCurrency: String,
        symbols: String
    ): Flow<ExchangeRate> {
        return flow {
            val defaultAmount = dataStoreRepositoryImpl.getAmount().getOrNull()!!
            val response =
                currencyApi.getLatestExchangeRates(
                    api_key = api_key,
                    baseCurrency = baseCurrency,
                    symbols = symbols
                )
            if (response.isSuccessful) {
                val exchangeRate = response.body()!!
                val currency = Currency(
                    name = "US Dollar",
                    symbol = "$",
                    date = exchangeRate.date,
                    rate = exchangeRate.rates["USD"]!!,
                    timestamp = exchangeRate.timestamp,
                    amount = defaultAmount,
                    from = exchangeRate.base,
                    to = "USD",
                    result = defaultAmount.times(exchangeRate.rates["USD"]!!),
                    success = exchangeRate.success,
                    isFirst = true
                )
                currencyDao.insertCurrency(currency)
                exchangeRateDao.insertLatestExchangeRates(exchangeRate = exchangeRate)
                emit(exchangeRateDao.getLatestExchangeRatesFromDb())
            }
        }.flowOn(IO)
    }

    override fun updateBaseCurrency(
        api_key: String,
        baseCurrency: String,
        symbols: String
    ): Flow<Unit> {
        return flow {
            val defaultAmount = dataStoreRepositoryImpl.getAmount().getOrNull()!!
            val response =
                currencyApi.getLatestExchangeRates(
                    api_key = api_key,
                    baseCurrency = baseCurrency,
                    symbols = symbols
                )
            if (response.isSuccessful) {
                dataStoreRepositoryImpl.setBaseCurrency(baseCurrency)
                val exchangeRate = response.body()!!
                val currenciesList = currencyDao.getAddedCurrencies().first()
                currenciesList.forEach { currency ->
                    currency.date = exchangeRate.date
                    currency.rate = exchangeRate.rates[currency.to]!!
                    currency.timestamp = exchangeRate.timestamp
                    currency.amount = defaultAmount
                    currency.from = exchangeRate.base
                    currency.result = defaultAmount.times(exchangeRate.rates[currency.to]!!)
                    currencyDao.insertCurrency(currency)
                }
                emit(Unit)
            }
        }.flowOn(IO)
    }
}