package com.codecollapse.currencyconverter.screens.currency

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codecollapse.currencyconverter.core.ui.currency.CurrencyUiState
import com.codecollapse.currencyconverter.core.ui.fluctuation.FluctuationUiState
import com.codecollapse.currencyconverter.data.model.currency.Currency
import com.codecollapse.currencyconverter.data.repository.CommonCurrencyRepository
import com.codecollapse.currencyconverter.data.repository.datastore.DataStoreRepositoryImpl
import com.codecollapse.currencyconverter.utils.Constants
import com.codecollapse.currencyconverter.utils.Resource
import com.codecollapse.currencyconverter.utils.asResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val commonCurrencyRepository: CommonCurrencyRepository,
    private val dataStoreRepositoryImpl: DataStoreRepositoryImpl
) :
    ViewModel() {

    private val _defaultCurrency = MutableStateFlow(runBlocking { Currency() })

    var defaultCurrency: StateFlow<Currency> = _defaultCurrency.asStateFlow()
    val currencyUiState: StateFlow<CurrencyUiState> =
        commonCurrencyRepository.getCurrencyList(
        ).asResource().map {
            when (it) {
                is Resource.Success -> {
                    CurrencyUiState.Success(
                        countryItem = it.data.toList()
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
        viewModelScope.launch(IO) {
            dataStoreRepositoryImpl.setBaseCurrency(currency)
        }
    }

    fun getCurrency(isFirst: Boolean) {
        viewModelScope.launch {
            _defaultCurrency.value = commonCurrencyRepository.getCurrency(isFirst).first()
        }
    }


    val fluctuationUiState : StateFlow<FluctuationUiState> =
        commonCurrencyRepository.checkFluctuation(
            Constants.API_KEY,
            runBlocking { dataStoreRepositoryImpl.getBaseCurrency().getOrNull() }!!,
            runBlocking { dataStoreRepositoryImpl.getTargetCurrency().getOrNull() }!!,
            Constants.START_DATE,
            Constants.END_DATE
        ).asResource().map {
            when (it) {
                is Resource.Loading -> {
                    FluctuationUiState.Loading
                }

                is Resource.Success -> {
                    FluctuationUiState.Success(it.data)
                }

                is Resource.Error -> {
                    Timber.d("FluctuationUiState.Error ${it.exception!!.message}")
                    FluctuationUiState.Error
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FluctuationUiState.Loading
        )

}