package com.codecollapse.currencyconverter.core.ui.convert

import com.codecollapse.currencyconverter.data.model.rateConverter.RateConverter

sealed interface ConvertRatesUiState {
    data class Success(val rateConverter: RateConverter) : ConvertRatesUiState
    data object Error : ConvertRatesUiState
    data object Loading : ConvertRatesUiState
}