package com.codecollapse.currencyconverter.core.ui.convert

import com.codecollapse.currencyconverter.data.model.rateConverter.RateConverter

sealed interface RateConverterUiState {
    data class Success(val rateConverter: RateConverter) : RateConverterUiState
    object Error : RateConverterUiState
    object Loading : RateConverterUiState
}