package com.codecollapse.currencyconverter.network

import com.codecollapse.currencyconverter.data.model.currency.fluctuation.CurrencyFluctuation
import com.codecollapse.currencyconverter.data.model.exchangeRate.ExchangeRate
import com.codecollapse.currencyconverter.data.model.rateConverter.RateConverter
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.text.DateFormatSymbols
import javax.inject.Singleton

@Singleton
interface CurrencyApi {

    @GET("latest")
    suspend fun getLatestExchangeRates(
        @Query("apikey") api_key: String,
        @Query("base") baseCurrency: String,
        @Query("symbols") symbols: String = ""
    ): Response<ExchangeRate>

    @GET("convert")
    suspend fun rateConversion(
        @Query("apikey") api_key: String,
        @Query("from") from : String,
        @Query("to") to : String,
        @Query("amount") amount : Int
    ) : Response<RateConverter>

    @GET("fluctuation")
    suspend fun checkFluctuation(
        @Query("apikey") api_key: String,
        @Query("base") baseCurrency: String,
        @Query("symbols") symbols: String,
        @Query("start_date") start_date : String,
        @Query("end_date") end_date : String
    ) : Response<CurrencyFluctuation>
}