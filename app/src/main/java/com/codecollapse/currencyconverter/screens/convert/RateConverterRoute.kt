package com.codecollapse.currencyconverter.screens.convert

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.codecollapse.currencyconverter.R
import com.codecollapse.currencyconverter.components.RateConvertComposable
import com.codecollapse.currencyconverter.components.ToRateConvertComposable
import com.codecollapse.currencyconverter.core.DestinationRoute
import com.codecollapse.currencyconverter.core.ui.convert.RateConverterUiState
import com.codecollapse.currencyconverter.data.model.currency.Currency
import com.codecollapse.currencyconverter.screens.ExchangeRateViewModel
import com.codecollapse.currencyconverter.screens.currency.CurrencyViewModel

@Composable
fun RateConverterRoute(
    navController: NavController,
    exchangeRateViewModel: ExchangeRateViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        exchangeRateViewModel.getAddedCurrencies()
    }
   /* val rateConverterUiState by exchangeRateViewModel.rateConverterUiState
        .collectAsStateWithLifecycle()*/
    val enterAmount by exchangeRateViewModel.enterAmount.collectAsStateWithLifecycle()
    val fromCountryCode by exchangeRateViewModel.fromCountryCode.collectAsStateWithLifecycle()
    val currencies = exchangeRateViewModel.currencies.value
    val listState = rememberLazyListState()
    Column(modifier = Modifier.fillMaxSize()) {
        RateConvertComposable(
            navController = navController,
            amount = enterAmount,
            fromCountry = fromCountryCode,
            exchangeRateViewModel = exchangeRateViewModel
        )
        /*ToRateConvertComposable(
            navController = navController,
            updatedRate = updatedRate
        )*/
        Spacer(modifier = Modifier.height(20.dp))

        if (currencies.isEmpty().not()) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                state = listState,
                contentPadding = PaddingValues(16.dp)
            ) {
                items(currencies.size) {
                    AddedCurrencyComposable(
                        currency = currencies[it]
                    )
                }
            }

        }
        Button(modifier = Modifier.padding(12.dp), onClick = {
            navController.navigate("currency_screen_route/".plus(false))
        }) {
            Text(text = "Add Currency")
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
   /* when (rateConverterUiState) {
        RateConverterUiState.Loading -> {
        }

        is RateConverterUiState.Success -> {

            // val rate = (rateConverterUiState as RateConverterUiState.Success).rateConverter


        }

        RateConverterUiState.Error -> {
        }
    }*/
}

@Composable
fun AddedCurrencyComposable(
    currency: Currency
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .wrapContentHeight()
            .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {

            Box {
                Row() {
                    Image(
                        painter = painterResource(id = R.drawable.pk),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .size(18.dp)
                            .clip(CircleShape)
                            .border(0.5.dp, color = Color.LightGray, CircleShape)
                    )
                    Text(
                        text = currency.to,
                        modifier = Modifier
                            .align(alignment = Alignment.CenterVertically)
                            .padding(start = 8.dp),
                        style = TextStyle(
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Light
                        )
                    )
                }
            }

            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
                Text(
                    modifier = Modifier.align(Alignment.End),
                    text = currency.result.toString()
                )
                Text(
                    modifier = Modifier.align(Alignment.End),
                    text = "1 ${currency.from} = ${currency.rate} ${currency.to}"
                )
            }
        }
    }
}
