package com.codecollapse.currencyconverter.core.ui.currency

import com.codecollapse.currencyconverter.data.model.currency.CommonCurrency

sealed interface CurrencyUiState {
    data class Success(val countryItem: Array<CommonCurrency>) : CurrencyUiState {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Success

            if (!countryItem.contentEquals(other.countryItem)) return false

            return true
        }

        override fun hashCode(): Int {
            return countryItem.contentHashCode()
        }
    }

    object Error : CurrencyUiState
    object Loading : CurrencyUiState
}