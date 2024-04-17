package com.codecollapse.currencyconverter.ui.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codecollapse.currencyconverter.core.ui.convert.RateConverterUiState
import com.codecollapse.currencyconverter.data.model.currency.Currency
import com.codecollapse.currencyconverter.data.repository.CommonCurrencyRepository
import com.codecollapse.currencyconverter.data.repository.datastore.DataStoreRepositoryImpl
import com.codecollapse.currencyconverter.utils.Resource
import com.codecollapse.currencyconverter.utils.asResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dataStoreRepositoryImpl: DataStoreRepositoryImpl,
    private val commonCurrencyRepository: CommonCurrencyRepository
) : ViewModel() {

    private val _enterAmount =
        MutableStateFlow(runBlocking { dataStoreRepositoryImpl.getAmount().getOrNull()!! })
    private val _fromCountryCode = MutableStateFlow(runBlocking {
        dataStoreRepositoryImpl.getBaseCurrency().getOrNull().orEmpty()
    })
    var enterAmount: StateFlow<Int> = _enterAmount.asStateFlow()
    var fromCountryCode: StateFlow<String> = _fromCountryCode.asStateFlow()
    val currencies: MutableState<List<Currency>> = mutableStateOf(arrayListOf())


    val rateConverterUiState: StateFlow<RateConverterUiState> =
        commonCurrencyRepository.getAddedCurrencies().asResource().map {
            when (it) {
                is Resource.Success -> {
                   if(it.data.isEmpty().not()){
                        currencies.value = it.data
                        _fromCountryCode.value = currencies.value.first().from

                    }
                    RateConverterUiState.Success(it.data.toList())
                }

                is Resource.Loading -> {
                    RateConverterUiState.Loading
                }

                is Resource.Error -> {
                    RateConverterUiState.Error
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = RateConverterUiState.Loading
        )

    fun updateRates(amount: Int) {
        viewModelScope.launch(IO) {
            dataStoreRepositoryImpl.setAmount(amount)
            currencies.value = commonCurrencyRepository.getCurrenciesWithUpdatedValues(amount)
            _fromCountryCode.value = currencies.value.first().from
            _enterAmount.value = amount
        }
    }

    fun removeCurrency(currencyName : String){
        viewModelScope.launch(IO) {
            commonCurrencyRepository.removeCurrency(currencyName)
        }
    }
}