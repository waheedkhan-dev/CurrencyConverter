package com.codecollapse.currencyconverter.core.ui.fluctuation

import com.codecollapse.currencyconverter.data.model.currency.fluctuation.CurrencyFluctuation

sealed interface FluctuationUiState {
    data class Success(val currencyFluctuation: CurrencyFluctuation) : FluctuationUiState

    object Error : FluctuationUiState
    object Loading : FluctuationUiState
}