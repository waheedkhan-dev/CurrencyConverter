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

    /*  @GET("games")
      suspend fun getGames(
          @Query("key") api_key : String,
          @Query("page") page : Int,
          @Query("page_size") page_size : Int
      ) : Response<GamesClass>

      @GET("genres")
      suspend fun getGamesCategories(
          @Query("key") api_key : String,
          @Query("page") page : Int
      ) : Response<GameGenres>

      @GET("games?ordering")
      suspend fun getTrendingGames(
          @Query("key") api_key: String,
          @Query("dates") page : String
      ) : Response<GamesClass>*/
}