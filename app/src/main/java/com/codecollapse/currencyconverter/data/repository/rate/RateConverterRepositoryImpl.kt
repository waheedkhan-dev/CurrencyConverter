package com.codecollapse.currencyconverter.data.repository.rate

import com.codecollapse.currencyconverter.data.model.currency.Currency
import com.codecollapse.currencyconverter.network.CurrencyApi
import com.codecollapse.currencyconverter.source.local.dao.CurrencyDao
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RateConverterRepositoryImpl @Inject constructor(
    private val currencyApi: CurrencyApi,
    private val currencyDao: CurrencyDao
) : RateConverterRepository {
    override fun rateConversion(
        api_key: String,
        from: String,
        to: String,
        amount: Int
    ): Flow<Currency> {
        return flow {
            val response =
                currencyApi.rateConversion(api_key = api_key, from = from, to = to, amount = amount)
            if (response.isSuccessful) {
                val rateConverter = response.body()!!
                val currency = Currency(
                    date = rateConverter.date,
                    rate = rateConverter.info.rate,
                    timestamp = rateConverter.info.timestamp,
                    amount = rateConverter.query.amount,
                    from = rateConverter.query.from,
                    to = rateConverter.query.to,
                    result = rateConverter.query.amount.times(rateConverter.info.rate),
                    success = rateConverter.success,
                    isFirst = true
                )
                currencyDao.insertCurrency(currency)
                emit(currency)
            }
        }.flowOn(IO)
    }
}