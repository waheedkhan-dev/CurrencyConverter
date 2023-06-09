package com.codecollapse.currencyconverter.data.model.currency

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency_table")
data class Currency(
    var name : String,
    var symbol : String,
    var date: String,
    var rate: Double,
    var timestamp: Int,
    var amount: Int,
    var from: String,
    @PrimaryKey
    var to: String,
    var result: Double,
    var isoCode : String,
    val success: Boolean,
    val isFirst : Boolean
){
    constructor() : this(name = "",symbol = "",date = "", rate = 0.0, timestamp = 0, amount = 0, from = "", to = "", result = 0.0, isoCode = "", success = false,isFirst = false)
}