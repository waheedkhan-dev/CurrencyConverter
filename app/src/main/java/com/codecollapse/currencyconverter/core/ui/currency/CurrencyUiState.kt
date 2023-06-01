package com.codecollapse.currencyconverter.core.ui.currency

import com.codecollapse.currencyconverter.data.model.currency.CommonCurrency

sealed interface CurrencyUiState {
    data class Success(val countryItem: List<CommonCurrency>) : CurrencyUiState

    object Error : CurrencyUiState
    object Loading : CurrencyUiState
}