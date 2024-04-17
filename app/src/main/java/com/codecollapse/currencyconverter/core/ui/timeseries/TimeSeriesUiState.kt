package com.codecollapse.currencyconverter.core.ui.timeseries

import com.codecollapse.currencyconverter.data.model.currency.timeseries.TimeSeries

sealed interface TimeSeriesUiState {
    data class Success(val timeSeries: List<TimeSeries>) : TimeSeriesUiState
    data object Error : TimeSeriesUiState
    data object Loading : TimeSeriesUiState
}