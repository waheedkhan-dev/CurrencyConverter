package com.codecollapse.currencyconverter.data.repository.rate

import com.codecollapse.currencyconverter.data.model.rateConverter.RateConverter
import kotlinx.coroutines.flow.Flow

interface RateConverterRepository {

    fun rateConversion(
        api_key: String,
        from: String = "USD",
        to: String = "GBP",
        amount: Int = 1
    ): Flow<RateConverter>
}