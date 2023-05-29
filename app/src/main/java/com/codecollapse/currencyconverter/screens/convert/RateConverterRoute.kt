package com.codecollapse.currencyconverter.screens.convert

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.codecollapse.currencyconverter.components.RateConvertComposable
import com.codecollapse.currencyconverter.core.ui.convert.RateConverterUiState
import com.codecollapse.currencyconverter.screens.ExchangeRateViewModel

@Composable
fun RateConverterRoute(
    navController: NavController,
    exchangeRateViewModel: ExchangeRateViewModel = hiltViewModel()
) {

    RateConvertComposable(
        navController = navController,
        countrySymbol = "PKR",//rate.query.from,
        _result = 300,
        exchangeRateViewModel = exchangeRateViewModel
    )

  /*  val rateConverterUiState by exchangeRateViewModel.rateConverterUiState
        .collectAsStateWithLifecycle()
    val defaultAmountState by exchangeRateViewModel.defaultAmountState
    val toAmountState by exchangeRateViewModel.toAmountState
    when (rateConverterUiState) {
        RateConverterUiState.Loading -> {
        }

        is RateConverterUiState.Success -> {
            val rate = (rateConverterUiState as RateConverterUiState.Success).rateConverter
            Column(modifier = Modifier.fillMaxSize()) {
                RateConvertComposable(
                    navController = navController,
                    countrySymbol = rate.query.from,
                    _result = defaultAmountState,
                    exchangeRateViewModel = exchangeRateViewModel
                )
                RateConvertComposable(
                    navController = navController,
                    countrySymbol = rate.query.to,
                    _result = toAmountState.toInt(),
                    exchangeRateViewModel = exchangeRateViewModel
                )
            }

        }

        RateConverterUiState.Error -> {

        }
    }*/
}
