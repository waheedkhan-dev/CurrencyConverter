package com.codecollapse.currencyconverter.screens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codecollapse.currencyconverter.core.ui.convert.RateConverterUiState
import com.codecollapse.currencyconverter.data.model.currency.Currency
import com.codecollapse.currencyconverter.data.model.rateConverter.UpdatedRate
import com.codecollapse.currencyconverter.data.repository.CommonCurrencyRepository
import com.codecollapse.currencyconverter.data.repository.datastore.DataStoreRepositoryImpl
import com.codecollapse.currencyconverter.data.repository.rate.RateConverterRepositoryImpl
import com.codecollapse.currencyconverter.utils.Constants
import com.codecollapse.currencyconverter.utils.Resource
import com.codecollapse.currencyconverter.utils.asResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
class ExchangeRateViewModel @Inject constructor(
    private val dataStoreRepositoryImpl: DataStoreRepositoryImpl,
    private val rateConverterRepositoryImpl: RateConverterRepositoryImpl,
    private val commonCurrencyRepository: CommonCurrencyRepository
) : ViewModel() {

    private val _enterAmount = MutableStateFlow( runBlocking { dataStoreRepositoryImpl.getAmount().getOrNull()!! })
    private val _fromCountryCode = MutableStateFlow(runBlocking { dataStoreRepositoryImpl.getBaseCurrency().getOrNull().orEmpty() })
    var enterAmount: StateFlow<Int> = _enterAmount.asStateFlow()
    var fromCountryCode: StateFlow<String> = _fromCountryCode.asStateFlow()
    val currencies: MutableState<List<Currency>> = mutableStateOf(arrayListOf())


/*    fun getLatestRate(){
        viewModelScope.launch {
            rateConverterRepositoryImpl.rateConversion(
                api_key = Constants.API_KEY,
                from = runBlocking { dataStoreRepositoryImpl.getBaseCurrency().getOrNull().orEmpty() },
                to = runBlocking { dataStoreRepositoryImpl.getTargetCurrency().getOrNull().orEmpty() },
                amount = runBlocking { dataStoreRepositoryImpl.getAmount().getOrNull()!! }
            ).asResource().collect {
                when (it) {
                    is Resource.Success -> {

                    }

                    is Resource.Loading -> {

                    }

                    is Resource.Error -> {

                    }
                }
            }
        }
    }*/
    fun updateRates(amount: Int) {
        viewModelScope.launch(IO) {
            dataStoreRepositoryImpl.setAmount(amount)
            currencies.value = commonCurrencyRepository.getCurrenciesWithUpdatedValues(amount)
            _fromCountryCode.value = currencies.value.first().from
            _enterAmount.value = amount
        }
    }

    fun getAddedCurrencies() {
        viewModelScope.launch(IO) {
            commonCurrencyRepository.getAddedCurrencies().collect {
                if(it.isEmpty().not()){
                    currencies.value = it
                    _fromCountryCode.value = currencies.value.first().from
                   // _enterAmount.value = currencies.value.first().amount
                }
            }
        }
    }
}