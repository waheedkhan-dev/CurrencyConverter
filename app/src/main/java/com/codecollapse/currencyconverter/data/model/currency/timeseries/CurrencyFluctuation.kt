package com.codecollapse.currencyconverter.data.model.currency.timeseries

data class CurrencyFluctuation(
    val base: String,
    val end_date: String,
    val fluctuation: Boolean,
    val rates: Rates,
    val start_date: String,
    val success: Boolean
)