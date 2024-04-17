package com.codecollapse.currencyconverter.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.codecollapse.currencyconverter.ui.screens.chart.ChartScreen
import com.codecollapse.currencyconverter.ui.screens.currency.CountryRoute
import com.codecollapse.currencyconverter.ui.screens.currency.CurrencyViewModel
import com.codecollapse.currencyconverter.ui.screens.home.HomeScreen
import com.codecollapse.currencyconverter.ui.screens.home.HomeViewModel

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        route = Graph.MAIN,
        startDestination = Destinations.HomeScreen.route

    ) {
        composable(route = Destinations.HomeScreen.route) {
            val homeViewModel: HomeViewModel = hiltViewModel()
            val rateConverterUiState = homeViewModel.rateConverterUiState
                .collectAsStateWithLifecycle().value
            val amount  = homeViewModel.enterAmount.collectAsStateWithLifecycle().value
            val fromCountryCode = homeViewModel.fromCountryCode.collectAsStateWithLifecycle().value
            val currencyList = homeViewModel.currencies.value

            HomeScreen(
                rateConverterUiState = rateConverterUiState,
                currenciesList = currencyList,
                amount = amount,
                fromCountryCode = fromCountryCode,
                onAddCurrencyButtonClicked = {
                    navController.navigate(Destinations.CurrencyScreen.route.plus("/").plus(it))
                },
                onCardClicked = {
                    navController.navigate(Destinations.CurrencyScreen.route.plus("/").plus(it))
                },
                onLongPress = {
                    homeViewModel.removeCurrency(it)
                }, onValueChanged = {
                    homeViewModel.updateRates(it.toInt())
                })
        }
        composable(route = Destinations.ChartScreen.route) {
            val currencyViewModel: CurrencyViewModel = hiltViewModel()
            LaunchedEffect(Unit) {
                currencyViewModel.getRateConversion()
                currencyViewModel.getHistoricalRates()
            }
            val rateConversionUiState =  currencyViewModel.rateConversion
                .collectAsStateWithLifecycle().value
            val timeSeriesUiState = currencyViewModel.historicalRates.collectAsStateWithLifecycle().value
            val currencyList = currencyViewModel.getCurrencyList()
            val isFromCountrySelected = currencyViewModel.isFromCountrySelected.value
            ChartScreen(
                rateConversionUiState = rateConversionUiState,
                timeSeriesState = timeSeriesUiState,
                currencyList = currencyList,
                isFromCountrySelected = isFromCountrySelected,
                setIsFromCountrySelected = {
                    currencyViewModel.setIsFromCountrySelected(it)
                },
                fromCountryChange = {
                    currencyViewModel.setFromCountry(it)
                    currencyViewModel.getRateConversion(fromCountry = it)
                    currencyViewModel.getHistoricalRates(fromCountry = it)
                },
                toCountryChange = {
                    currencyViewModel.setToCountry(it)
                    currencyViewModel.getRateConversion(toCountry = it)
                    currencyViewModel.getHistoricalRates(toCountry = it)
                })
        }

        composable(route = Destinations.CurrencyScreen.route.plus("/{isChangingCurrency}"),
            arguments = listOf(
                navArgument("isChangingCurrency") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )) {
            val isChangingCurrency = it.arguments?.getBoolean("isChangingCurrency") ?: false
            val currencyViewModel: CurrencyViewModel = hiltViewModel()
            LaunchedEffect(Unit) {
                currencyViewModel.getCurrency(true)
            }
            val list = currencyViewModel.getCurrencyList()
            val defaultSelectedCurrency = currencyViewModel.defaultCurrency.collectAsStateWithLifecycle().value
            CountryRoute(isChangingCurrency = isChangingCurrency,
                defaultSelectedCurrency = defaultSelectedCurrency,
                list = list,
                updateCurrency = { code ->
                    currencyViewModel.updatedBaseCurrency(code)
                    navController.popBackStack()
                },
                addCurrency = { name, code, symbol, isoCode ->
                    currencyViewModel.addCurrency(name, code, symbol, isoCode)
                    navController.popBackStack()
                })
        }
    }
}


object Graph {

    const val MAIN = "main_graph"
}

sealed class Destinations(val route: String) {
    data object HomeScreen : Destinations("home")
    data object ChartScreen : Destinations("chart")
    data object CurrencyScreen : Destinations("currency_screen")
}