package com.codecollapse.currencyconverter.data.model.currency.timeseries

data class USD(
    val change: Double,
    val change_pct: Double,
    val end_rate: Double,
    val start_rate: Double
)