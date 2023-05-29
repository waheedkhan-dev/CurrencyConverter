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
import com.codecollapse.currencyconverter.components.ToRateConvertComposable
import com.codecollapse.currencyconverter.core.ui.convert.RateConverterUiState
import com.codecollapse.currencyconverter.screens.ExchangeRateViewModel

@Composable
fun RateConverterRoute(
    navController: NavController,
    exchangeRateViewModel: ExchangeRateViewModel = hiltViewModel()
) {

    val rateConverterUiState by exchangeRateViewModel.rateConverterUiState
        .collectAsStateWithLifecycle()
    val updatedRate by exchangeRateViewModel.updatedRate.collectAsStateWithLifecycle()

    when (rateConverterUiState) {
        RateConverterUiState.Loading -> {
        }

        is RateConverterUiState.Success -> {

            val rate = (rateConverterUiState as RateConverterUiState.Success).rateConverter
            Column(modifier = Modifier.fillMaxSize()) {
                RateConvertComposable(
                    navController = navController,
                    updatedRate = updatedRate,
                    exchangeRateViewModel = exchangeRateViewModel
                )
                ToRateConvertComposable(
                    navController = navController,
                    updatedRate = updatedRate
                )
            }

        }

        RateConverterUiState.Error -> {
        }
    }
}
