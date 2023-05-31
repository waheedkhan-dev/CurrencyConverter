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

    @Query("SELECT * FROM currency_table Where isFirst=:isFirst")
    fun getCurrency(isFirst : Boolean) : Currency
    @Query("SELECT * FROM currency_table ORDER BY isFirst DESC")
    fun getAddedCurrencies(): List<Currency>
}