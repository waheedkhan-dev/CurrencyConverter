package com.codecollapse.currencyconverter.data.model.rateConverter

data class RateConverter(
    val date: String,
    val info: Info,
    val query: Query,
    val result: Double,
    val success: Boolean
)