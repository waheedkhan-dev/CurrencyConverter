package com.codecollapse.currencyconverter.screens.currency

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.codecollapse.currencyconverter.core.DestinationRoute

fun NavGraphBuilder.countryNavGraph(navController: NavController) {
    composable(route = DestinationRoute.COUNTRY_SCREEN_ROUTE) {
        CountryRoute(navController)
    }
}