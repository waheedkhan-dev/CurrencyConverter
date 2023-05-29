package com.codecollapse.currencyconverter.screens.rates

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.codecollapse.currencyconverter.core.DestinationRoute

fun NavGraphBuilder.rateNavGraph(navController: NavController) {
    composable(route = DestinationRoute.RATE_SCREEN_ROUTE) {
        ExchangeRateRoute(navController)
    }
}