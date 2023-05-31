package com.codecollapse.currencyconverter.screens.currency

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codecollapse.currencyconverter.core.ui.currency.CurrencyUiState
import com.codecollapse.currencyconverter.data.model.currency.Currency
import com.codecollapse.currencyconverter.data.repository.CommonCurrencyRepository
import com.codecollapse.currencyconverter.data.repository.datastore.DataStoreRepositoryImpl
import com.codecollapse.currencyconverter.utils.Resource
import com.codecollapse.currencyconverter.utils.asResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val commonCurrencyRepository: CommonCurrencyRepository,
    private val dataStoreRepositoryImpl: DataStoreRepositoryImpl
) :
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

    fun addCurrency(currency: Currency) {
        viewModelScope.launch(IO) {
            commonCurrencyRepository.addCurrency(currency = currency)
        }
    }

    fun updatedBaseCurrency(currency: String) {
        viewModelScope.launch(IO){
            dataStoreRepositoryImpl.setBaseCurrency(currency)
        }
    }

}