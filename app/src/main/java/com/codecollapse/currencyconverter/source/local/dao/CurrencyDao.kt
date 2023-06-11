package com.codecollapse.currencyconverter.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.codecollapse.currencyconverter.data.model.currency.Currency
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrency(currency: Currency)

    @Query("SELECT * FROM currency_table Where isFirst=:isFirst")
    fun getCurrency(isFirst : Boolean) : Currency
    @Query("SELECT * FROM currency_table ORDER BY isFirst DESC")
    fun getAddedCurrencies(): Flow<List<Currency>>

    @Query("DELETE FROM currency_table where `to`=:currencyName")
    fun removeCurrency(currencyName : String)
}