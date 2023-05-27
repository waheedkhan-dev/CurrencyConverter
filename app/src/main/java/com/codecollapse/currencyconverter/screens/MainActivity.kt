@file:OptIn(ExperimentalMaterial3Api::class)

package com.codecollapse.currencyconverter.screens

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.codecollapse.currencyconverter.R
import com.codecollapse.currencyconverter.components.CurrencyTextField
import com.codecollapse.currencyconverter.ui.theme.CurrencyConverterTheme
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
            CurrencyConverterTheme {
                TopBarComposable()
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TopBarComposable() {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(text = stringResource(id = R.string.convert))
                }
            )
        }
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(it)) {
            CurrencyTextField()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TopBarPreviewer() {
    TopBarComposable()
}
