package com.codecollapse.currencyconverter

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.SwipeableDefaults
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.codecollapse.currencyconverter.components.BottomBar
import com.codecollapse.currencyconverter.core.DestinationRoute.CONVERT_SCREEN_ROUTE
import com.codecollapse.currencyconverter.core.DestinationRoute.RATE_SCREEN_ROUTE
import com.codecollapse.currencyconverter.navigation.AppNavHost
import com.codecollapse.currencyconverter.ui.theme.CurrencyConverterTheme
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout

import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterialNavigationApi::class
)
@Composable
fun RootScreen() {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberNavController(bottomSheetNavigator)
    val currentBackStackEntryAsState by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntryAsState?.destination
    val context = LocalContext.current


    val isShowBottomBar = when (currentDestination?.route) {
        CONVERT_SCREEN_ROUTE, RATE_SCREEN_ROUTE, null -> true
        else -> false
    }
    val darkMode = when (currentDestination?.route) {
        CONVERT_SCREEN_ROUTE, RATE_SCREEN_ROUTE, null -> true
        else -> false
    }

    if (currentDestination?.route == CONVERT_SCREEN_ROUTE) {
        BackHandler {
            (context as? Activity)?.finish()
        }
    }

    CurrencyConverterTheme(darkTheme = darkMode) {

       // SetupSystemUi(rememberSystemUiController(), MaterialTheme.colorScheme.primary)
        ModalBottomSheetLayout(bottomSheetNavigator = bottomSheetNavigator) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        modifier = Modifier.fillMaxWidth(),
                        title = {
                            Text(text = stringResource(id = R.string.convert))
                        }
                    )
                },
                bottomBar = {
                    if (!isShowBottomBar) {
                        return@Scaffold
                    }
                    BottomBar(navController, currentDestination, isDarkTheme = darkMode)
                }
            ) {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White)
                    .padding(it)) {
                    AppNavHost(navController = navController)
                }
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

@OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalMaterialApi::class)
@Composable
fun rememberBottomSheetNavigator(
    animationSpec: AnimationSpec<Float> = SwipeableDefaults.AnimationSpec
): BottomSheetNavigator {
    val sheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden,
        animationSpec
    )
    return remember(sheetState) {
        BottomSheetNavigator(sheetState = sheetState)
    }
}


