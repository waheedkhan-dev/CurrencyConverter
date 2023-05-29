package com.codecollapse.currencyconverter.core.ui.convert

import com.codecollapse.currencyconverter.data.model.rateConverter.UpdatedRate

sealed interface RateConverterUiState {
    data class Success(val rateConverter: UpdatedRate) : RateConverterUiState
    object Error : RateConverterUiState
    object Loading : RateConverterUiState
}