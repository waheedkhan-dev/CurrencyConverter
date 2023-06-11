package com.codecollapse.currencyconverter.data.repository

import android.content.Context
import com.codecollapse.currencyconverter.R
import com.codecollapse.currencyconverter.data.model.currency.CommonCurrency
import com.codecollapse.currencyconverter.data.model.currency.Currency
import com.codecollapse.currencyconverter.data.model.currency.fluctuation.CurrencyFluctuation
import com.codecollapse.currencyconverter.data.repository.datastore.DataStoreRepositoryImpl
import com.codecollapse.currencyconverter.data.repository.exchange.ExchangeRateRepositoryImpl
import com.codecollapse.currencyconverter.network.CurrencyApi
import com.codecollapse.currencyconverter.source.local.dao.CurrencyDao
import com.codecollapse.currencyconverter.source.local.dao.ExchangeRateDao
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
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
    private val exchangeRateDao: ExchangeRateDao,
    private val currencyApi: CurrencyApi,
    private val currencyDao: CurrencyDao
) {

    fun getCurrencyList(): Array<CommonCurrency> {
        val inputStream: InputStream =
            context.resources.openRawResource(R.raw.common_currency)
        val jsonString = inputStream.bufferedReader().use(BufferedReader::readText)
        inputStream.close()
        return Gson().fromJson(jsonString, Array<CommonCurrency>::class.java)
    }

    fun checkFluctuation(
        api_key: String,
        baseCurrency: String,
        symbols: String,
        start_date: String,
        end_date: String
    ): Flow<CurrencyFluctuation> {
        return flow {
            val response =
                currencyApi.checkFluctuation(api_key, baseCurrency, symbols, start_date, end_date)
            if (response.isSuccessful) {
                emit(response.body()!!)
            }
        }
    }

    fun addCurrency(name:String,code : String,symbols: String,isoCode : String) {
        val amount = runBlocking { dataStoreRepositoryImpl.getAmount().getOrNull() }!!
        val rateAsPerBaseCurrency = exchangeRateDao.getLatestExchangeRatesFromDb()
        val currencyMatch = rateAsPerBaseCurrency.rates.filter {
            it.key == code
        }
        if (currencyMatch.isEmpty().not()) {
            val currency = Currency()
            currency.name = name
            currency.symbol = symbols
            currency.to = code
            currency.isoCode = isoCode
            currency.from = rateAsPerBaseCurrency.base
            currency.date = rateAsPerBaseCurrency.date
            currency.rate = currencyMatch[currency.to]!!
            currency.result = amount.times(currency.rate)
            currency.amount = amount
            currency.timestamp = rateAsPerBaseCurrency.timestamp
            currencyDao.insertCurrency(currency = currency)
        }
    }

    fun getAddedCurrencies(): Flow<List<Currency>> = flow {
        currencyDao.getAddedCurrencies().collect {
            emit(it)
        }
    }.flowOn(IO)

    suspend fun getCurrenciesWithUpdatedValues(amount: Int): List<Currency> {
        val currencies = currencyDao.getAddedCurrencies().first()
        currencies.forEach { currency ->
            currency.result = currency.rate.times(amount)
        }
        return currencies
    }

    fun getCurrency(isFirst: Boolean): Flow<Currency> {
        return flow {
            emit(currencyDao.getCurrency(isFirst))
        }.flowOn(IO)
    }

    fun removeCurrency(currencyName : String){
        currencyDao.removeCurrency(currencyName)
    }
}