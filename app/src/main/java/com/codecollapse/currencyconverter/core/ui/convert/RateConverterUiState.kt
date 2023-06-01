package com.codecollapse.currencyconverter.core.ui.convert

import com.codecollapse.currencyconverter.data.model.currency.Currency

sealed interface RateConverterUiState {
    data class Success(val currency : List<Currency>) : RateConverterUiState
    object Error : RateConverterUiState
    object Loading : RateConverterUiState
}