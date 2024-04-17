package com.codecollapse.currencyconverter.ui.screens.currency

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codecollapse.currencyconverter.core.ui.convert.ConvertRatesUiState
import com.codecollapse.currencyconverter.core.ui.timeseries.TimeSeriesUiState
import com.codecollapse.currencyconverter.data.model.currency.Currency
import com.codecollapse.currencyconverter.data.repository.CommonCurrencyRepository
import com.codecollapse.currencyconverter.data.repository.datastore.DataStoreRepositoryImpl
import com.codecollapse.currencyconverter.data.repository.exchange.ExchangeRateRepositoryImpl
import com.codecollapse.currencyconverter.data.repository.rate.RateConverterRepositoryImpl
import com.codecollapse.currencyconverter.utils.Constants
import com.codecollapse.currencyconverter.utils.Resource
import com.codecollapse.currencyconverter.utils.asResource
import com.codecollapse.currencyconverter.utils.getDateFromMill
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val commonCurrencyRepository: CommonCurrencyRepository,
    private val rateConverterRepositoryImpl: RateConverterRepositoryImpl,
    private val exchangeRateRepositoryImpl: ExchangeRateRepositoryImpl,
    private val dataStoreRepositoryImpl: DataStoreRepositoryImpl
) :
    ViewModel() {

    private val currentDate = Calendar.getInstance()
    private var startDateCalendar = Calendar.getInstance()

    init {
        startDateCalendar.add(Calendar.DAY_OF_MONTH, -6)
    }

    /*private val _changeRateValue = MutableStateFlow("past month")
    var changeRateValue: StateFlow<String> = _changeRateValue.asStateFlow()*/
    private var _fromCountry = runBlocking {
        dataStoreRepositoryImpl.getFromCountry().getOrNull()!!
    }
    private var _toCountry = runBlocking { dataStoreRepositoryImpl.getToCountry().getOrNull() }!!
    var isFromCountrySelected = mutableStateOf(false)
    private val _defaultCurrency = MutableStateFlow(runBlocking { Currency() })
    var defaultCurrency: StateFlow<Currency> = _defaultCurrency.asStateFlow()
    private val _rateConversion = MutableStateFlow<ConvertRatesUiState>(ConvertRatesUiState.Loading)
    var rateConversion: StateFlow<ConvertRatesUiState> = _rateConversion.asStateFlow()


    private val _historicalRates= MutableStateFlow<TimeSeriesUiState>(TimeSeriesUiState.Loading)
    var historicalRates: StateFlow<TimeSeriesUiState> = _historicalRates.asStateFlow()

    fun getCurrencyList() = commonCurrencyRepository.getCurrencyList()

    fun addCurrency(name: String, code: String, symbol: String, isoCode: String) {
        viewModelScope.launch(IO) {
            commonCurrencyRepository.addCurrency(name, code, symbol, isoCode)
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

    fun setIsFromCountrySelected(isSelected: Boolean) {
        isFromCountrySelected.value = isSelected
    }


    fun getRateConversion(
        fromCountry: String = _fromCountry,
        toCountry: String = _toCountry
    ) {
        viewModelScope.launch {
            rateConverterRepositoryImpl.rateConversion(
                Constants.API_KEY,
                fromCountry,
                toCountry,
                1
            ).asResource().map {
                when (it) {
                    is Resource.Loading -> {
                        _rateConversion.value = ConvertRatesUiState.Loading
                    }

                    is Resource.Success -> {
                        // _changeRateValue.value = "${it.data.first().rates} past month"

                        _rateConversion.value = ConvertRatesUiState.Success(it.data)

                    }

                    is Resource.Error -> {
                        Timber.d("FluctuationUiState.Error ${it.exception!!.message}")
                        _rateConversion.value = ConvertRatesUiState.Error

                    }
                }
            }.collect{}
        }

    }

    fun getHistoricalRates(fromCountry: String = _fromCountry, toCountry: String = _toCountry){
        viewModelScope.launch {
            commonCurrencyRepository.checkTimeSeries(
                Constants.API_KEY,
                fromCountry,
                toCountry,
                getDateFromMill(startDateCalendar.timeInMillis),
                getDateFromMill(currentDate.timeInMillis)
            ).asResource().map { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _historicalRates.update {
                            TimeSeriesUiState.Loading
                        }
                    }

                    is Resource.Success -> {
                        _historicalRates.update {
                            TimeSeriesUiState.Success(resource.data)
                        }
                        //    _changeRateValue.value = "${it.data.first().rates} past month"

                    }

                    is Resource.Error -> {
                        Timber.d("FluctuationUiState.Error ${resource.exception!!.message}")
                        _historicalRates.update {
                            TimeSeriesUiState.Error
                        }

                    }
                }
            }.collect{}
        }

    }

    fun setFromCountry(code: String) {
        viewModelScope.launch(IO) {
            dataStoreRepositoryImpl.setFromCountry(code)

        }
    }

    fun setToCountry(code: String) {
        viewModelScope.launch(IO) {
            dataStoreRepositoryImpl.setToCountry(code)
        }
    }
}