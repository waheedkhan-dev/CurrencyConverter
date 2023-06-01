package com.codecollapse.currencyconverter.utils.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ExchangeRateConverter {
    @TypeConverter
    fun fromString(value: String): Map<String, Double> {
        val type = object : TypeToken<Map<String, Double>>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun toString(value: Map<String, Double>): String {
        return Gson().toJson(value)
    }
}