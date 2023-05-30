package com.codecollapse.currencyconverter.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.codecollapse.currencyconverter.R
import com.codecollapse.currencyconverter.core.DestinationRoute
import com.codecollapse.currencyconverter.data.model.rateConverter.UpdatedRate
import com.codecollapse.currencyconverter.screens.ExchangeRateViewModel


@Composable
fun RateConvertComposable(
    navController: NavController,
    updatedRate: UpdatedRate, exchangeRateViewModel: ExchangeRateViewModel
) {
    val focusManager = LocalFocusManager.current
    var updatedValue by remember { mutableStateOf(updatedRate.amount.toString()) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .wrapContentHeight()
            .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {

            Box(
                modifier = Modifier.clickable {
                    navController.navigate(DestinationRoute.CURRENCY_SCREEN_ROUTE)
                }
            ) {
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
                        text = updatedRate.from,
                        modifier = Modifier
                            .align(alignment = Alignment.CenterVertically)
                            .padding(start = 8.dp),
                        style = TextStyle(
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Light
                        )
                    )
                    Icon(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        painter = painterResource(id = R.drawable.round_keyboard_arrow_down_24),
                        contentDescription = "arrow_down",
                        tint = Color.LightGray
                    )
                }
            }
            BasicTextField(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f),
                value = updatedValue,//_//result.toString(),
                onValueChange = {
                    updatedValue = it
                },
                textStyle = TextStyle(textAlign = TextAlign.End),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number
                ),
                keyboardActions = KeyboardActions(onDone = {
                    exchangeRateViewModel.updateRates(updatedRate.from, updatedRate.to, updatedValue.toInt())
                    focusManager.clearFocus()
                })
            )

            /*  Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
                  Text(modifier = Modifier.align(Alignment.End), text = "$0.21")
                  Text(modifier = Modifier.align(Alignment.End), text = "1 PKR = 0.0035 USD")
              }*/
        }
    }
}


@Composable
fun ToRateConvertComposable(
    navController: NavController,
    updatedRate: UpdatedRate
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
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
                        text = updatedRate.to,
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
                    text = updatedRate.result.toString()
                )
                Text(
                    modifier = Modifier.align(Alignment.End),
                    text = "1 ${updatedRate.from} = ${updatedRate.rate} ${updatedRate.to}"
                )
            }
        }
    }
}

