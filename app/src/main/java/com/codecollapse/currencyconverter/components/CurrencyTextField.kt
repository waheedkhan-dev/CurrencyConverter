package com.codecollapse.currencyconverter.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.codecollapse.currencyconverter.R
import com.codecollapse.currencyconverter.screens.ExchangeRateViewModel


@Composable
fun RateConvertComposable(
    navController: NavController,
    amount: Int,
    fromCountry: String,
    exchangeRateViewModel: ExchangeRateViewModel
) {
    val focusManager = LocalFocusManager.current
    var updatedValue by remember { mutableStateOf(amount.toString()) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .background(colorResource(id = R.color.welcome_color), RoundedCornerShape(12.dp))
            .wrapContentHeight()
            .border(1.dp, colorResource(id = R.color.welcome_color), RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {

            Box(
                modifier = Modifier.clickable {
                    navController.navigate("currency_screen_route/".plus(true))
                },
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {

                    Text(
                        text = fromCountry,
                        style = TextStyle(
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.montserrat_bold, FontWeight.W300)),
                            color = colorResource(id = R.color.color_header_text)
                        )
                    )
                    Icon(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        painter = painterResource(id = R.drawable.round_keyboard_arrow_down_24),
                        contentDescription = "arrow_down",
                        tint = colorResource(id = R.color.color_header_text)
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
                textStyle = TextStyle(
                    color = colorResource(id = R.color.color_header_text),
                    textAlign = TextAlign.End,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.montserrat_semi_bold, FontWeight.W600)),
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number
                ),
                keyboardActions = KeyboardActions(onDone = {
                    if (updatedValue.isNotBlank()) {
                        exchangeRateViewModel.updateRates(updatedValue.toInt())
                        focusManager.clearFocus()
                    }
                })
            )

            Icon(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(4.dp),
                painter = painterResource(id = R.drawable.calculator_),
                contentDescription = "calculator",
                tint = colorResource(id = R.color.card_background_color)
            )
        }
    }
}
