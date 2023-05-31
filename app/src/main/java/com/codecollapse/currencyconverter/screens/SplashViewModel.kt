package com.codecollapse.currencyconverter.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codecollapse.currencyconverter.data.repository.datastore.DataStoreRepositoryImpl
import com.codecollapse.currencyconverter.data.repository.exchange.ExchangeRateRepositoryImpl
import com.codecollapse.currencyconverter.data.repository.rate.RateConverterRepositoryImpl
import com.codecollapse.currencyconverter.utils.Constants
import com.codecollapse.currencyconverter.utils.Resource
import com.codecollapse.currencyconverter.utils.asResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val exchangeRateRepositoryImpl: ExchangeRateRepositoryImpl,
    private val dataStoreRepository: DataStoreRepositoryImpl
) :
    ViewModel() {

    private val _isDeviceSync =
        MutableStateFlow(runBlocking { dataStoreRepository.getIsDeviceSync().getOrNull()!! })
    val isDeviceSync = _isDeviceSync.asStateFlow()

    init {
        syncDevice()
    }

    private fun syncDevice() {
        val baseCurrency =
            runBlocking { dataStoreRepository.getBaseCurrency().getOrNull().orEmpty() }
        if (isDeviceSync.value.not()) {
            viewModelScope.launch {
                exchangeRateRepositoryImpl.getLatestExchangeRates(
                    api_key = Constants.API_KEY,
                    baseCurrency = baseCurrency
                ).asResource().collect {
                    when (it) {
                        is Resource.Success -> {
                            dataStoreRepository.isDeviceSync(true)
                            _isDeviceSync.value = true
                        }

                        is Resource.Loading -> {

                        }

                        is Resource.Error -> {
                            Timber.d("Error ${it.exception}")
                        }
                    }
                }
            }

        } else {
            _isDeviceSync.value = true
        }
    }
}