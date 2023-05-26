package com.codecollapse.currencyconverter.data.model.country

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "country_table")
data class CountryItem(
    @PrimaryKey
    val country: String,
    val currency_code: String,
    val isSelected : Boolean
)