package com.codecollapse.currencyconverter.utils.converter

import androidx.room.TypeConverter
import com.codecollapse.currencyconverter.data.model.exchangeRate.Rates
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ExchangeRateConverter {

    @TypeConverter
    fun responseToString(Response: Rates): String {
        return Gson().toJson(Response)
    }

    @TypeConverter
    fun stringToResponse(data: String): Rates {
        val listType = object : TypeToken<Rates>() {}.type
        return Gson().fromJson(data, listType)
    }
}