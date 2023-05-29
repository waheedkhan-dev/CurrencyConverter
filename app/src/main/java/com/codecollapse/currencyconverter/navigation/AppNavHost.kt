package com.codecollapse.currencyconverter.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.codecollapse.currencyconverter.core.DestinationRoute.CONVERT_SCREEN_ROUTE
import com.codecollapse.currencyconverter.screens.convert.convertNavGraph
import com.codecollapse.currencyconverter.screens.currency.countryNavGraph
import com.codecollapse.currencyconverter.screens.rates.rateNavGraph

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = CONVERT_SCREEN_ROUTE
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        convertNavGraph(navController)
        rateNavGraph(navController)
        countryNavGraph(navController)
    }
}