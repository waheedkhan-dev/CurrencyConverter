package com.codecollapse.currencyconverter.data.model.exchangeRate

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.codecollapse.currencyconverter.utils.converter.ExchangeRateConverter

@Entity(tableName = "exchange_rate_table")
data class ExchangeRate(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 1,
    val base: String,
    val date: String,
    @TypeConverters(ExchangeRateConverter::class)
    val rates: Rates,
    val success: Boolean,
    val timestamp: Int
)