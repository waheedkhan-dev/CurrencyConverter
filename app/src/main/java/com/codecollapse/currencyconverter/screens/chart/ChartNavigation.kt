package com.codecollapse.currencyconverter.screens.chart

import androidx.compose.material.ExperimentalMaterialApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.codecollapse.currencyconverter.core.DestinationRoute

@OptIn(ExperimentalMaterialApi::class)
fun NavGraphBuilder.chartNavGraph(navController: NavController) {
    composable(route = DestinationRoute.CHART_SCREEN_ROUTE) {
        ChartRoute(navController)
    }
}