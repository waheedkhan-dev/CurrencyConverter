package com.codecollapse.currencyconverter.screens.currency

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codecollapse.currencyconverter.core.ui.currency.CurrencyUiState
import com.codecollapse.currencyconverter.data.repository.CommonCurrencyRepository
import com.codecollapse.currencyconverter.utils.Resource
import com.codecollapse.currencyconverter.utils.asResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(commonCurrencyRepository: CommonCurrencyRepository) :
    ViewModel() {

    val currencyUiState: StateFlow<CurrencyUiState> =
        commonCurrencyRepository.getCurrencyList(
        ).asResource().map {
            when (it) {
                is Resource.Success -> {
                    CurrencyUiState.Success(
                        countryItem = it.data
                    )
                }

                is Resource.Loading -> {
                    CurrencyUiState.Loading
                }

                is Resource.Error -> {
                    CurrencyUiState.Error
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CurrencyUiState.Loading
        )
}