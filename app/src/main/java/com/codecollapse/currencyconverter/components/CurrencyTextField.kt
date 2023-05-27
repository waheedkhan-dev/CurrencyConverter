@file:OptIn(ExperimentalMaterial3Api::class)

package com.codecollapse.currencyconverter.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codecollapse.currencyconverter.R
import com.codecollapse.currencyconverter.core.ui.convert.RateConverterUiState
import com.codecollapse.currencyconverter.data.model.rateConverter.RateConverter
import com.codecollapse.currencyconverter.screens.MainViewModel

@Composable
fun CurrencyTextField(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = viewModel()
) {
    val rateConverterUiState by mainViewModel.rateConverterUiState.collectAsStateWithLifecycle()
    when (rateConverterUiState) {
        RateConverterUiState.Loading -> {
        }

        is RateConverterUiState.Success -> {
            RateConvertComposable(
                rateConverter = (rateConverterUiState as RateConverterUiState.Success).rateConverter,
                mainViewModel = mainViewModel
            )
            ToRateConverterComposable(rateConverter = (rateConverterUiState as RateConverterUiState.Success).rateConverter)
        }

        RateConverterUiState.Error -> {

        }
    }
}

@Composable
fun RateConvertComposable(rateConverter: RateConverter, mainViewModel: MainViewModel) {
    val focusManager = LocalFocusManager.current
    var value by remember {
        mutableStateOf(rateConverter.query.amount.toString())
    }
    var isDonePress by remember {
        mutableStateOf(false)
    }
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
                text = rateConverter.query.from,//stringResource(R.string.pkr),
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

            BasicTextField(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f),
                value = value,
                onValueChange = {
                    value = it
                },
                textStyle = TextStyle(textAlign = TextAlign.End),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number
                ),
                keyboardActions = KeyboardActions(onDone = {
                    isDonePress = true
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
fun ToRateConverterComposable(rateConverter: RateConverter) {
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
                text = rateConverter.query.to,
                modifier = Modifier
                    .align(alignment = Alignment.CenterVertically)
                    .padding(start = 8.dp),
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Light
                )
            )
            /* Icon(
                 modifier = Modifier.align(Alignment.CenterVertically),
                 painter = painterResource(id = R.drawable.round_keyboard_arrow_down_24),
                 contentDescription = "arrow_down",
                 tint = Color.LightGray
             )*/
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
                Text(
                    modifier = Modifier.align(Alignment.End),
                    text = rateConverter.result.toString()
                )
                Text(
                    modifier = Modifier.align(Alignment.End),
                    text = "1 ${rateConverter.query.to} = ${rateConverter.info.rate} ${rateConverter.query.from}"
                )
            }
        }
    }
}


