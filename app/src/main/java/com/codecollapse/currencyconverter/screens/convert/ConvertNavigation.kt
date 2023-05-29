package com.codecollapse.currencyconverter.screens.convert

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.codecollapse.currencyconverter.core.DestinationRoute.CONVERT_SCREEN_ROUTE

fun NavGraphBuilder.convertNavGraph(navController: NavController) {
    composable(route = CONVERT_SCREEN_ROUTE) {
        RateConverterRoute(navController)
    }
}