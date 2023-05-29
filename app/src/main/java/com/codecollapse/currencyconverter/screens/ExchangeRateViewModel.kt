package com.codecollapse.currencyconverter.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codecollapse.currencyconverter.core.ui.convert.RateConverterUiState
import com.codecollapse.currencyconverter.data.model.rateConverter.UpdatedRate
import com.codecollapse.currencyconverter.data.repository.datastore.DataStoreRepositoryImpl
import com.codecollapse.currencyconverter.data.repository.rate.RateConverterRepositoryImpl
import com.codecollapse.currencyconverter.utils.Constants
import com.codecollapse.currencyconverter.utils.Resource
import com.codecollapse.currencyconverter.utils.asResource
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val rateConverterRepositoryImpl: RateConverterRepositoryImpl
) : ViewModel() {

    private val _updatedRateState = MutableStateFlow(
        UpdatedRate()
    )

    var updatedRate: StateFlow<UpdatedRate> = _updatedRateState.asStateFlow()

    val rateConverterUiState: StateFlow<RateConverterUiState> =
        rateConverterRepositoryImpl.rateConversion(
            api_key = Constants.API_KEY,
            from = runBlocking { dataStoreRepositoryImpl.getBaseCurrency().getOrNull().orEmpty() },
            to = runBlocking { dataStoreRepositoryImpl.getTargetCurrency().getOrNull().orEmpty() },
            amount = runBlocking { dataStoreRepositoryImpl.getAmount().getOrNull()!! }
        ).asResource().map {
            when (it) {
                is Resource.Success -> {
                    _updatedRateState.value = it.data
                    RateConverterUiState.Success(
                        rateConverter = it.data
                    )
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

    fun updateRates(from: String, to: String, amount: Int) {
        viewModelScope.launch {
            rateConverterRepositoryImpl.rateConversion(
                api_key = Constants.API_KEY,
                from = from,
                to = to,
                amount = amount
            ).asResource().collect {
                when (it) {
                    is Resource.Success -> {
                        _updatedRateState.value = it.data
                    }

                    is Resource.Loading -> {

                    }

                    is Resource.Error -> {

                    }
                }
            }
        }
    }
}