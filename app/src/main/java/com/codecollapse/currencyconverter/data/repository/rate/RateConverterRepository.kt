package com.codecollapse.currencyconverter.data.repository.rate

import com.codecollapse.currencyconverter.data.model.rateConverter.UpdatedRate
import kotlinx.coroutines.flow.Flow

interface RateConverterRepository {

    fun rateConversion(
        api_key: String,
        from: String = "PKR",
        to: String = "USD",
        amount: Int = 1
    ): Flow<UpdatedRate>
}