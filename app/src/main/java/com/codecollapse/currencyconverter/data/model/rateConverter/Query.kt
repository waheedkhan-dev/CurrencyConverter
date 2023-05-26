package com.codecollapse.currencyconverter.data.model.rateConverter

data class Query(
    val amount: Int,
    val from: String,
    val to: String
)