package com.codecollapse.currencyconverter.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.codecollapse.currencyconverter.core.DestinationRoute.CONVERT_SCREEN_ROUTE
import com.codecollapse.currencyconverter.screens.convert.convertNavGraph
import com.codecollapse.currencyconverter.screens.currency.currencyNavGraph
import com.codecollapse.currencyconverter.screens.chart.chartNavGraph

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
    startDestination: String = CONVERT_SCREEN_ROUTE
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        convertNavGraph(navController)
        chartNavGraph(navController)
        currencyNavGraph(navController)
    }
}