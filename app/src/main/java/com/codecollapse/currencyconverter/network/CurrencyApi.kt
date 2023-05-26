package com.codecollapse.currencyconverter.network

import com.codecollapse.currencyconverter.data.model.exchangeRate.ExchangeRate
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface CurrencyApi {

    @GET("latest")
    suspend fun getLatestExchangeRates(
        @Query("apikey") api_key: String,
        @Query("base") baseCurrency: String
    ): Response<ExchangeRate>
}