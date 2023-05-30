package com.codecollapse.currencyconverter.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.codecollapse.currencyconverter.data.model.currency.Currency

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrency(currency: Currency)

    @Query("SELECT * FROM currency_table")
    fun getAddedCurrencies(): List<Currency>
}