package com.codecollapse.currencyconverter.core.ui

import com.codecollapse.currencyconverter.data.model.exchangeRate.ExchangeRate

sealed interface ExchangeRateUiState {
    data class Success(val exchangeRate: ExchangeRate) : ExchangeRateUiState
    object Error : ExchangeRateUiState
    object Loading : ExchangeRateUiState
}