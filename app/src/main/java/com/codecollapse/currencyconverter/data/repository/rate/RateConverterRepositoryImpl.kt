package com.codecollapse.currencyconverter.data.repository.rate

import com.codecollapse.currencyconverter.data.model.rateConverter.RateConverter
import com.codecollapse.currencyconverter.network.CurrencyApi
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RateConverterRepositoryImpl @Inject constructor(
    private val currencyApi: CurrencyApi
) : RateConverterRepository {
    override fun rateConversion(
        api_key: String,
        from: String,
        to: String,
        amount: Int
    ): Flow<RateConverter> {
        return flow {
            val response =
                currencyApi.rateConversion(api_key = api_key, from = from, to = to, amount = amount)
            if (response.isSuccessful) {
                emit(response.body()!!)
            }
        }.flowOn(IO)
    }
}