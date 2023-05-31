package com.codecollapse.currencyconverter.screens.convert

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.codecollapse.currencyconverter.R
import com.codecollapse.currencyconverter.components.RateConvertComposable
import com.codecollapse.currencyconverter.data.model.currency.Currency
import com.codecollapse.currencyconverter.screens.ExchangeRateViewModel
import com.codecollapse.currencyconverter.utils.previewProvider.CurrencyProvider

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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.card_background_color))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(160.dp)
                .background(
                    colorResource(id = R.color.background_color)
                )
        ) {
            Column() {
                Text(
                    modifier = Modifier.padding(12.dp),
                    text = "Welcome,Back",
                    style = TextStyle(
                        textAlign = TextAlign.Center,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.card_background_color)
                    )
                )
                Spacer(modifier = Modifier.height(18.dp))
                RateConvertComposable(
                    navController = navController,
                    amount = enterAmount,
                    fromCountry = fromCountryCode,
                    exchangeRateViewModel = exchangeRateViewModel
                )
            }

        }

        /*ToRateConvertComposable(
            navController = navController,
            updatedRate = updatedRate
        )*/
        Spacer(modifier = Modifier.height(8.dp))

        if (currencies.isEmpty().not()) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                state = listState,
                contentPadding = PaddingValues(8.dp)
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

@Preview(showBackground = true)
@Composable
fun AddedCurrencyComposable(
    @PreviewParameter(CurrencyProvider::class) currency: Currency
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .wrapContentHeight()
            .background(color = Color.White)
            .border(
                1.dp,
                color = colorResource(id = R.color.color_sub_text),
                RoundedCornerShape(12.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {

            Box {
                Column() {
                    /* Image(
                         painter = painterResource(id = R.drawable.pk),
                         contentDescription = "",
                         contentScale = ContentScale.Crop,
                         modifier = Modifier
                             .align(Alignment.CenterVertically)
                             .size(18.dp)
                             .clip(CircleShape)
                             .border(0.5.dp, color = Color.LightGray, CircleShape)
                     )*/
                    Text(
                        text = currency.to,
                        style = TextStyle(
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = colorResource(id = R.color.color_header_text)
                        )
                    )
                    Text(
                        text = currency.name,
                        style = TextStyle(
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = colorResource(id = R.color.color_sub_text)
                        )
                    )
                }
            }

            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
                Text(
                    modifier = Modifier.align(Alignment.End),
                    text = "${currency.symbol} ${currency.result}",
                    style = TextStyle(
                        color = colorResource(id = R.color.color_header_text),
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Text(
                    modifier = Modifier.align(Alignment.End),
                    text = "1 ${currency.from} = ${currency.rate} ${currency.to}",
                    style = TextStyle(
                        color = colorResource(id = R.color.color_sub_text),
                        textAlign = TextAlign.Center,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}
