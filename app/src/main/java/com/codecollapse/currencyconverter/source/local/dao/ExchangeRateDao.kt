package com.codecollapse.currencyconverter.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.codecollapse.currencyconverter.data.model.exchangeRate.ExchangeRate

@Dao
interface ExchangeRateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLatestExchangeRates(exchangeRate: ExchangeRate)
}