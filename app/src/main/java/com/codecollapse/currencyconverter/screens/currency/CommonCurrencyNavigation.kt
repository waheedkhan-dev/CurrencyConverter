package com.codecollapse.currencyconverter.screens.currency

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.codecollapse.currencyconverter.core.DestinationRoute

fun NavGraphBuilder.currencyNavGraph(navController: NavController) {
    composable(route = DestinationRoute.CURRENCY_SCREEN_ROUTE, arguments = listOf(
        navArgument("isChangingCurrency") {
            type = NavType.BoolType
            defaultValue = false
        }
    )) {
        val isChangingCurrency = it.arguments!!.getBoolean("isChangingCurrency")
        CountryRoute(navController, isChangingCurrency)
    }
}