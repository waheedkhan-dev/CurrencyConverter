package com.codecollapse.currencyconverter.core.ui.convert

import com.codecollapse.currencyconverter.data.model.rateConverter.RateConverter

sealed interface ConvertRatesUiState {
    data class Success(val rateConverter: RateConverter) : ConvertRatesUiState
    object Error : ConvertRatesUiState
    object Loading : ConvertRatesUiState
}