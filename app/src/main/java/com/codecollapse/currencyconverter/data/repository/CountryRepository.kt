package com.codecollapse.currencyconverter.data.repository

import android.content.Context
import com.codecollapse.currencyconverter.R
import com.codecollapse.currencyconverter.data.model.country.CountryItem
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.BufferedReader
import java.io.InputStream
import javax.inject.Inject

class CountryRepository @Inject constructor() {

    fun getCountryList(context: Context): Flow<ArrayList<CountryItem>> = flow {
        val inputStream: InputStream =
            context.resources.openRawResource(R.raw.country_by_currency_code)
        val jsonString = inputStream.bufferedReader().use(BufferedReader::readText)
        inputStream.close()
        emit(Gson().fromJson(jsonString, arrayListOf<CountryItem>()::class.java))
    }.flowOn(IO)
}