package com.codecollapse.currencyconverter.ui.screens.home

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.blongho.country_data.World
import com.codecollapse.currencyconverter.R
import com.codecollapse.currencyconverter.core.ui.convert.RateConverterUiState
import com.codecollapse.currencyconverter.data.model.currency.Currency
import com.codecollapse.currencyconverter.ui.components.RateConvertComposable
import com.codecollapse.currencyconverter.utils.previewProvider.CurrencyProvider
import timber.log.Timber

@Composable
fun HomeScreen(
    rateConverterUiState: RateConverterUiState,
    currenciesList: List<Currency>,
    amount: Int,
    fromCountryCode: String,
    onCardClicked: (Boolean) -> Unit,
    onAddCurrencyButtonClicked: (Boolean) -> Unit,
    onLongPress: (String) -> Unit,
    onValueChanged: (String) -> Unit

) {

    val listState = rememberLazyListState()

    when (rateConverterUiState) {
        RateConverterUiState.Loading -> {
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                CircularProgressIndicator(modifier = Modifier.align(alignment = Alignment.CenterHorizontally))
            }
        }

        is RateConverterUiState.Success -> {
            var currencies = rateConverterUiState.currency
            if (currenciesList.isEmpty().not()) {
                currencies = currenciesList
            }
            if (currencies.isEmpty().not()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colorResource(id = R.color.card_background_color))
                ) {

                    item {
                        Column {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .requiredHeight(160.dp)
                                    .background(
                                        colorResource(id = R.color.background_color)
                                    )
                            ) {
                                Column {
                                    Text(
                                        modifier = Modifier.padding(12.dp),
                                        text = "Welcome,Back",
                                        style = TextStyle(
                                            textAlign = TextAlign.Center,
                                            fontSize = 22.sp,
                                            fontFamily = FontFamily(
                                                Font(
                                                    R.font.montserrat_semi_bold,
                                                    FontWeight.W400
                                                )
                                            ),
                                            color = colorResource(id = R.color.card_background_color)
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(18.dp))
                                    RateConvertComposable(
                                        amount = amount,
                                        fromCountry = fromCountryCode,
                                        onCardClicked = {
                                            onCardClicked(true)
                                        }, onValueChange = {
                                            onValueChanged(it)
                                        }
                                    )
                                }

                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .requiredHeightIn(50.dp, 600.dp),
                                state = listState,
                                contentPadding = PaddingValues(8.dp)
                            ) {
                                items(currencies.size) {
                                    AddedCurrencyComposable(
                                        currency = currencies[it],
                                        onLongPress = { currency ->
                                            onLongPress(currency)
                                        }

                                    )
                                }
                            }
                            Button(modifier = Modifier
                                .padding(12.dp),
                                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.to_light_green)),
                                shape = RoundedCornerShape(8.dp),
                                onClick = {
                                    onAddCurrencyButtonClicked(false)
                                    //  navController.navigate("currency_screen_route/".plus(false))
                                }) {
                                Row(
                                    verticalAlignment = CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {

                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_add_24),
                                        contentDescription = stringResource(
                                            R.string.button_icon
                                        ),
                                        tint = colorResource(
                                            id = R.color.color_header_text
                                        )
                                    )
                                    Text(
                                        text = "Add currency",
                                        style = TextStyle(
                                            textAlign = TextAlign.Center,
                                            fontSize = 12.sp,
                                            fontFamily = FontFamily(
                                                Font(
                                                    R.font.montserrat_medium,
                                                    FontWeight.W300
                                                )
                                            ),
                                            color = colorResource(id = R.color.color_header_text)
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }

            }
        }

        RateConverterUiState.Error -> {
            Timber.tag("RateConverterUiState Error")
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddedCurrencyComposable(
    currency: Currency,
    onLongPress: (String) -> Unit
) {
    val context = LocalContext.current
    val message = stringResource(R.string.unable_to_remove_default_currency)
    val flag = World.getFlagOf(currency.isoCode)

    Box(
        modifier = Modifier
            .combinedClickable(
                onClick = {},
                onLongClick = {
                    if (currency.isFirst.not()) {
                        onLongPress(currency.to)
                    } else {
                        Toast
                            .makeText(context, message, Toast.LENGTH_SHORT)
                            .show()
                    }

                }
            )
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
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(flag)
                    .crossfade(true)
                    .build(),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.pk),
                contentDescription = stringResource(R.string.country_flg),
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .align(CenterVertically)
                    .border(1.dp, colorResource(id = R.color.background_color), CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))

            Box {
                Column() {
                    Text(
                        text = currency.to,
                        style = TextStyle(
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.montserrat_bold, FontWeight.W300)),
                            color = colorResource(id = R.color.color_header_text)
                        )
                    )
                    Text(
                        text = currency.name,
                        style = TextStyle(
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp,
                            fontFamily = FontFamily(
                                Font(
                                    R.font.montserrat_medium,
                                    FontWeight.W300
                                )
                            ),
                            color = colorResource(id = R.color.grey)
                        )
                    )
                }
            }

            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
                Text(
                    modifier = Modifier.align(Alignment.End),
                    text = "${currency.symbol} ${
                        String.format("%.3f", currency.result).toDouble()
                    }",
                    style = TextStyle(
                        color = colorResource(id = R.color.color_header_text),
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.montserrat_semi_bold, FontWeight.W500)),
                    )
                )
                Text(
                    modifier = Modifier.align(Alignment.End),
                    text = "1 ${currency.from} = ${currency.rate} ${currency.to}",
                    style = TextStyle(
                        color = colorResource(id = R.color.grey),
                        textAlign = TextAlign.Center,
                        fontSize = 11.sp,
                        fontFamily = FontFamily(Font(R.font.montserrat_medium, FontWeight.W800)),
                    )
                )
            }
        }
    }
}