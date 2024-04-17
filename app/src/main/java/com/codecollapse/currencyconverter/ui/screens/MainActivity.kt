@file:OptIn(ExperimentalMaterial3Api::class)

package com.codecollapse.currencyconverter.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.codecollapse.currencyconverter.RootScreen
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                splashViewModel.isDeviceSync.value.not()
            }

            setOnExitAnimationListener { splashScreenView ->
                splashScreenView.remove()
            }
        }
        setContent {
            RootScreen()
        }
    }
}
