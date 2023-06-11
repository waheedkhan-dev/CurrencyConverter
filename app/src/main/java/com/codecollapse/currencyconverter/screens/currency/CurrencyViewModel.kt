package com.codecollapse.currencyconverter.screens.currency

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codecollapse.currencyconverter.core.ui.fluctuation.FluctuationUiState
import com.codecollapse.currencyconverter.data.model.currency.Currency
import com.codecollapse.currencyconverter.data.repository.CommonCurrencyRepository
import com.codecollapse.currencyconverter.data.repository.datastore.DataStoreRepositoryImpl
import com.codecollapse.currencyconverter.data.repository.exchange.ExchangeRateRepositoryImpl
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
    private val exchangeRateRepositoryImpl: ExchangeRateRepositoryImpl,
    private val dataStoreRepositoryImpl: DataStoreRepositoryImpl
) :
    ViewModel() {

    private val _defaultCurrency = MutableStateFlow(runBlocking { Currency() })
    var defaultCurrency: StateFlow<Currency> = _defaultCurrency.asStateFlow()
    private val _changeRateValue = MutableStateFlow("past month")
    var changeRateValue : StateFlow<String> = _changeRateValue.asStateFlow()

    fun getCurrencyList() = commonCurrencyRepository.getCurrencyList()

    fun addCurrency(name: String, code: String, symbol: String,isoCode : String) {
        viewModelScope.launch(IO) {
            commonCurrencyRepository.addCurrency(name, code, symbol,isoCode)
        }
    }

    fun updatedBaseCurrency(currency: String) {
        exchangeRateRepositoryImpl.updateBaseCurrency(Constants.API_KEY, currency, "")
    }

    fun getCurrency(isFirst: Boolean) {
        viewModelScope.launch {
            _defaultCurrency.value = commonCurrencyRepository.getCurrency(isFirst).first()
        }
    }


    val fluctuationUiState: StateFlow<FluctuationUiState> =
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
                    _changeRateValue.value = "${it.data.rates.USD.change} past month"
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