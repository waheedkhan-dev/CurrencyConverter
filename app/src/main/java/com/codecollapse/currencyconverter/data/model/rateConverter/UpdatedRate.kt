package com.codecollapse.currencyconverter.data.model.rateConverter

data class UpdatedRate(
    val date: String,
    val rate: Double,
    val timestamp: Int,
    val amount: Int,
    val from: String,
    val to: String,
    val result: Double,
    val success: Boolean
) {
    constructor() : this(date = "", rate = 0.0, timestamp = 0, amount = 0, from = "", to = "", result = 0.0, success = false)
}