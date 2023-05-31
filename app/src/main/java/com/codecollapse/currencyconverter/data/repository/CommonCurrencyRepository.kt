package com.codecollapse.currencyconverter.data.repository

import android.content.Context
import com.codecollapse.currencyconverter.R
import com.codecollapse.currencyconverter.data.model.currency.CommonCurrency
import com.codecollapse.currencyconverter.data.model.currency.Currency
import com.codecollapse.currencyconverter.data.repository.datastore.DataStoreRepositoryImpl
import com.codecollapse.currencyconverter.data.repository.exchange.ExchangeRateRepositoryImpl
import com.codecollapse.currencyconverter.source.local.dao.CurrencyDao
import com.codecollapse.currencyconverter.utils.Constants
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.runBlocking
import java.io.BufferedReader
import java.io.InputStream
import javax.inject.Inject


class CommonCurrencyRepository @Inject constructor(
    @ApplicationContext var context: Context,
    private val exchangeRateRepositoryImpl: ExchangeRateRepositoryImpl,
    private val dataStoreRepositoryImpl: DataStoreRepositoryImpl,
    private val currencyDao: CurrencyDao
) {

    fun getCurrencyList(): Flow<Array<CommonCurrency>> = flow {
        val inputStream: InputStream =
            context.resources.openRawResource(R.raw.common_currency)
        val jsonString = inputStream.bufferedReader().use(BufferedReader::readText)
        inputStream.close()
        val personArray = Gson().fromJson(jsonString, Array<CommonCurrency>::class.java)
        emit(personArray)
    }.flowOn(IO)


    fun addCurrency(currency: Currency) {
        currencyDao.insertCurrency(currency = currency)
    }

    fun getAddedCurrencies(): Flow<List<Currency>> = flow<List<Currency>> {
        val currenciesList = arrayListOf<Currency>()
        val baseCurrency = runBlocking { dataStoreRepositoryImpl.getBaseCurrency().getOrNull() }!!
        val amount = runBlocking { dataStoreRepositoryImpl.getAmount().getOrNull() }!!
        val currencies = currencyDao.getAddedCurrencies()
        var symbols = ""
        currencies.forEach {
            symbols = symbols.plus(it.to).plus(",")
        }

        exchangeRateRepositoryImpl.getLatestExchangeRates(
            Constants.API_KEY, baseCurrency, symbols
        ).collect { exchangeRate ->
            if (currencies.isEmpty().not()) {
                currencies.forEachIndexed { _, currency ->
                    currency.date = exchangeRate.date
                    currency.rate = exchangeRate.rates[currency.to]!!
                    currency.timestamp = exchangeRate.timestamp
                    currency.amount = amount
                    currency.from = exchangeRate.base
                    currency.result = amount.times(exchangeRate.rates[currency.to]!!)
                    currencyDao.insertCurrency(currency)
                    currenciesList.add(currency)
                }
            } else {
                val currency = Currency(
                    date = exchangeRate.date,
                    rate = exchangeRate.rates["USD"]!!,
                    timestamp = exchangeRate.timestamp,
                    amount = amount,
                    from = exchangeRate.base,
                    to = "USD",
                    result = amount.times(exchangeRate.rates["USD"]!!),
                    success = exchangeRate.success,
                    isFirst = true
                )
                currencyDao.insertCurrency(currency)
                currenciesList.add(currency)
            }
        }
        emit(currenciesList)
    }.flowOn(IO)

    fun getCurrenciesWithUpdatedValues(amount: Int): List<Currency> {
        val currencies = currencyDao.getAddedCurrencies()
        currencies.forEach { currency ->
            currency.result = currency.rate.times(amount)
        }
        return currencies
    }
}