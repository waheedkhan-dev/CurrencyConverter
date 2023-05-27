package com.codecollapse.currencyconverter.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codecollapse.currencyconverter.core.ui.convert.RateConverterUiState
import com.codecollapse.currencyconverter.data.repository.rate.RateConverterRepositoryImpl
import com.codecollapse.currencyconverter.source.datastore.DataStoreRepository
import com.codecollapse.currencyconverter.utils.Constants
import com.codecollapse.currencyconverter.utils.Resource
import com.codecollapse.currencyconverter.utils.asResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    dataStoreRepository: DataStoreRepository,
    rateConverterRepositoryImpl: RateConverterRepositoryImpl
) : ViewModel() {

    /*    var from = runBlocking { dataStoreRepository.getBaseCurrency.first() }
        var to = runBlocking { dataStoreRepository.getTargetCurrency.first() }
        var amount = runBlocking { dataStoreRepository.getAmount.first() }*/

    val rateConverterUiState: StateFlow<RateConverterUiState> =
        rateConverterRepositoryImpl.rateConversion(
            api_key = Constants.API_KEY,
            from = "PKR",
            to = "USD",
            amount = 500
        ).asResource().map {
            when (it) {
                is Resource.Success -> {
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


    /*  fun updateRates(from : String,to : String,amount : Int){
          viewModelScope.launch {
              rateConverterRepositoryImpl.rateConversion(api_key = Constants.API_KEY, from = from, to = to,amount = amount).collect{}
          }
      }*/
}