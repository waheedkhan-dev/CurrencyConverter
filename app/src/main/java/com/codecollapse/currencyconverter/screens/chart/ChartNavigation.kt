package com.codecollapse.currencyconverter.screens.chart

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.codecollapse.currencyconverter.core.DestinationRoute

fun NavGraphBuilder.chartNavGraph(navController: NavController) {
    composable(route = DestinationRoute.CHART_SCREEN_ROUTE) {
        ChartRoute(navController)
    }
}