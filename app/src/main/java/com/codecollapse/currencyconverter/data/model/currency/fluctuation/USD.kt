package com.codecollapse.currencyconverter.data.model.currency.fluctuation

data class USD(
    val change: Int,
    val change_pct: Double,
    val end_rate: Double,
    val start_rate: Double
)