package com.codecollapse.currencyconverter.data.repository

import android.content.Context
import com.codecollapse.currencyconverter.R
import com.codecollapse.currencyconverter.data.model.currency.CommonCurrency
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject
import timber.log.Timber
import java.io.BufferedReader
import java.io.InputStream
import javax.inject.Inject


class CommonCurrencyRepository @Inject constructor(@ApplicationContext var context: Context) {

    fun getCurrencyList(): Flow<Array<CommonCurrency>> = flow {
        val inputStream: InputStream =
            context.resources.openRawResource(R.raw.common_currency)
        val jsonString = inputStream.bufferedReader().use(BufferedReader::readText)
        inputStream.close()
        val personArray = Gson().fromJson(jsonString, Array<CommonCurrency>::class.java)
       /* for (person in personArray) {
            println("Name: ${person.name}, Age: ${person.code}")
        }
        val value = Gson().fromJson(jsonString, arrayListOf<CommonCurrency>() ::class.java)
        val list = arrayListOf<CommonCurrency>()
        value.forEach {
            list.add(it)
        }*/
        emit(personArray)

       /* val inputStream: InputStream =
            context.resources.openRawResource(R.raw.common_currency)
        val jsonString = inputStream.bufferedReader().use(BufferedReader::readText).trimIndent()
        inputStream.close()
        val currencies = Json.decodeFromString<Map<String, CommonCurrency>>(jsonString)
        for ((code, currency) in currencies) {
            println("Currency Code: $code")
            println("Symbol: ${currency.symbol}")
            println("Name: ${currency.name}")
        }
        emit(Gson().fromJson(jsonString, arrayListOf<CommonCurrency>()::class.java))*/
    }.flowOn(IO)

}