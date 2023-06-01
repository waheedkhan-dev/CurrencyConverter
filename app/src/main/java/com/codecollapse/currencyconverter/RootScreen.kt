package com.codecollapse.currencyconverter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.codecollapse.currencyconverter.components.BottomBar
import com.codecollapse.currencyconverter.navigation.AppNavHost
import com.codecollapse.currencyconverter.ui.theme.CurrencyConverterTheme
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@OptIn(
    ExperimentalMaterial3Api::class,
)
@Composable
fun RootScreen() {
    val navController = rememberNavController()
    val currentBackStackEntryAsState by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntryAsState?.destination
    val context = LocalContext.current

    CurrencyConverterTheme() {
        SetupSystemUi(rememberSystemUiController(), colorResource(id = R.color.background_color))
        Scaffold(
            topBar = {
                /*TopAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    title = {
                        Text(text = stringResource(id = R.string.convert))
                    }
                )*/
            },
            bottomBar = {
                BottomBar(navController, currentDestination)
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White)
                    .padding(it)
            ) {
                AppNavHost(navController = navController)
            }
        }
    }
}

@Composable
fun SetupSystemUi(
    systemUiController: SystemUiController,
    systemBarColor: Color
) {
    SideEffect {
        systemUiController.setSystemBarsColor(color = systemBarColor)
    }
}


