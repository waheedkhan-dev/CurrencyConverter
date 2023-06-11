package com.codecollapse.currencyconverter.screens.chart

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.codecollapse.currencyconverter.R
import com.codecollapse.currencyconverter.core.ui.fluctuation.FluctuationUiState
import com.codecollapse.currencyconverter.data.model.currency.CommonCurrency
import com.codecollapse.currencyconverter.data.model.currency.Currency
import com.codecollapse.currencyconverter.screens.currency.CurrencyViewModel
import com.codecollapse.currencyconverter.screens.currency.SearchEditText
import kotlinx.coroutines.CoroutineScope
import me.bytebeats.views.charts.line.LineChart
import me.bytebeats.views.charts.line.LineChartData
import me.bytebeats.views.charts.line.render.line.SolidLineDrawer
import me.bytebeats.views.charts.line.render.point.FilledCircularPointDrawer
import me.bytebeats.views.charts.line.render.xaxis.SimpleXAxisDrawer
import me.bytebeats.views.charts.line.render.yaxis.SimpleYAxisDrawer
import me.bytebeats.views.charts.simpleChartAnimation

@ExperimentalMaterialApi
@Composable
fun ChartRoute(
    navController: NavController,
    currencyViewModel: CurrencyViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        currencyViewModel.getCurrency(true)
        //currencyViewModel.checkFluctuation("PKR", "USD")
    }

    val fluctuationState by currencyViewModel.fluctuationUiState
        .collectAsStateWithLifecycle()
    val changeRateValue by currencyViewModel.changeRateValue.collectAsStateWithLifecycle()
    val currencyList = currencyViewModel.getCurrencyList()
    val defaultSelectedCurrency by currencyViewModel.defaultCurrency.collectAsStateWithLifecycle()
    BottomSheet(currencyList.toList(), fluctuationState, defaultSelectedCurrency,changeRateValue)

}

@ExperimentalMaterialApi
@Composable
private fun BottomSheet(
    currency: List<CommonCurrency>,
    fluctuationState: FluctuationUiState,
    defaultSelectedCurrency: Currency,
    changeRateValue : String
) {
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded },
        skipHalfExpanded = true
    )

    val scope = rememberCoroutineScope()
    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),//Rounded corners
        sheetElevation = 20.dp,//To provide Shadow
        sheetContent = {
            Column(
                modifier = Modifier
                    .heightIn(min = 100.dp, max = 500.dp)
                    .background(Color.White.copy(0.2f))
                    .padding(20.dp),
                horizontalAlignment = CenterHorizontally
            ) {
                Spacer(//Using this to create a kind of Icon that tells the user that the sheet is expandable
                    modifier = Modifier
                        .height(3.dp)
                        .width(70.dp)
                        .background(Color.LightGray)
                )
                Spacer(//Another spacer to add a space
                    modifier = Modifier
                        .height(12.dp)
                )
                Text(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(alignment = CenterHorizontally),
                    text = "Select currency", style = TextStyle(
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Normal,
                        fontSize = 18.sp,
                        color = colorResource(id = R.color.color_header_text)
                    )
                )


                var searchQuery by remember { mutableStateOf("") }
                SearchEditText(onSearchQueryChange = { newQuery ->
                    searchQuery = newQuery
                    // Perform search operation with newQuery
                })
                val filteredList = currency.filter { currency ->
                    currency.name.contains(searchQuery, ignoreCase = true)
                }
                if (filteredList.isEmpty().not()) {
                    CurrencyListComposable(currencyList = filteredList)
                } else {
                    CurrencyListComposable(currencyList = currency)
                }
            }
        }
    ) {
        DrawChartComposable(modalSheetState, scope, fluctuationState, defaultSelectedCurrency,changeRateValue)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DrawChartComposable(
    sheetState: ModalBottomSheetState,
    scope: CoroutineScope,
    fluctuationState: FluctuationUiState,
    defaultSelectedCurrency: Currency,
    changeRateValue : String
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
            .background(color = colorResource(id = R.color.card_background_color))
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier
                .padding(8.dp)
                .align(alignment = CenterHorizontally),
            text = "Chart", style = TextStyle(
                textAlign = TextAlign.Start,
                fontFamily = FontFamily(Font(R.font.montserrat_semi_bold, FontWeight.W400)),
                fontSize = 18.sp,
                color = colorResource(id = R.color.color_header_text)
            )
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Box(
                modifier = Modifier
                    .requiredWidth(150.dp)
                    .padding(12.dp)
                    .background(
                        colorResource(id = R.color.white),
                        RoundedCornerShape(12.dp)
                    )
                    .wrapContentHeight()
                    .border(
                        1.dp,
                        colorResource(id = R.color.color_sub_text),
                        RoundedCornerShape(12.dp)
                    )
                    .clickable {
                        /* scope.launch {
                            if (sheetState.isVisible)
                                sheetState.hide()
                            else
                                sheetState.show()
                        }*/
                    },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = defaultSelectedCurrency.from,
                        style = TextStyle(
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.montserrat_bold, FontWeight.W300)),
                            color = colorResource(id = R.color.color_header_text)
                        )
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Icon(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        painter = painterResource(id = R.drawable.round_keyboard_arrow_down_24),
                        contentDescription = "arrow_down",
                        tint = colorResource(id = R.color.color_header_text)
                    )

                }
            }

            Icon(
                modifier = Modifier.align(Alignment.CenterVertically),
                painter = painterResource(id = R.drawable.baseline_compare_arrows_24),
                contentDescription = "compare_arrows",
                tint = colorResource(id = R.color.background_color)
            )

            Box(
                modifier = Modifier
                    .requiredWidth(150.dp)
                    .padding(12.dp)
                    .background(
                        colorResource(id = R.color.white),
                        RoundedCornerShape(12.dp)
                    )
                    .wrapContentHeight()
                    .border(
                        1.dp,
                        colorResource(id = R.color.color_sub_text),
                        RoundedCornerShape(12.dp)
                    )
                    .clickable {
                        /* scope.launch {
                            if (sheetState.isVisible)
                                sheetState.hide()
                            else
                                sheetState.show()
                        }*/
                    },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = defaultSelectedCurrency.to,
                        style = TextStyle(
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.montserrat_bold, FontWeight.W300)),
                            color = colorResource(id = R.color.color_header_text)
                        )
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Icon(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        painter = painterResource(id = R.drawable.round_keyboard_arrow_down_24),
                        contentDescription = "arrow_down",
                        tint = colorResource(id = R.color.color_header_text)
                    )

                }
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    colorResource(id = R.color.to_light_green),
                    shape = RoundedCornerShape(12.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(alignment = CenterHorizontally),
                    text = "1 ${defaultSelectedCurrency.from} = ${defaultSelectedCurrency.rate} ${defaultSelectedCurrency.to}",
                    style = TextStyle(
                        textAlign = TextAlign.Start,
                        fontFamily = FontFamily(Font(R.font.montserrat_bold, FontWeight.W400)),
                        fontSize = 22.sp,
                        color = colorResource(id = R.color.color_header_text)
                    )
                )
                Text(
                    modifier = Modifier
                        .align(alignment = CenterHorizontally),
                    text = changeRateValue, style = TextStyle(
                        textAlign = TextAlign.Start,
                        fontFamily = FontFamily(Font(R.font.montserrat_medium, FontWeight.W200)),
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.background_color)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
        LineChartView(fluctuationState = fluctuationState)

    }
}

@Composable
fun CurrencyListComposable(currencyList: List<CommonCurrency>) {
    val listState = rememberLazyListState()
    var selectedItem by remember { mutableStateOf("") }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(16.dp)
    ) {

        items(currencyList.size) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier
                .fillMaxWidth()
                .selectable(
                    selected = selectedItem == currencyList[it].name, onClick = {
                        selectedItem = currencyList[it].name
                    }
                )) {
                Text(
                    text = "${currencyList[it].code}  -  ", style = TextStyle(
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.color_header_text)
                    )
                )
                Text(
                    text = currencyList[it].name, style = TextStyle(
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.color_sub_text)
                    )
                )

                /*  if (selectedItem == currencyList[it].name) {

                  }*/

            }
        }
    }
}

@Composable
fun LineChartView(fluctuationState: FluctuationUiState) {
    when (fluctuationState) {
        FluctuationUiState.Loading -> {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = CenterHorizontally
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(alignment = CenterHorizontally)
                )
            }
        }

        is FluctuationUiState.Success -> {
            LineChart(
                lineChartData = LineChartData(
                    points = listOf(
                        LineChartData.Point(
                            fluctuationState.currencyFluctuation.rates.USD.start_rate.toFloat(),
                            fluctuationState.currencyFluctuation.start_date
                        ),
                        LineChartData.Point(
                            fluctuationState.currencyFluctuation.rates.USD.end_rate.toFloat(),
                            fluctuationState.currencyFluctuation.end_date
                        ),
                    )
                ),
                modifier = Modifier.fillMaxSize(),
                animation = simpleChartAnimation(),
                pointDrawer = FilledCircularPointDrawer(),
                lineDrawer = SolidLineDrawer(),
                xAxisDrawer = SimpleXAxisDrawer(),
                yAxisDrawer = SimpleYAxisDrawer(),
                horizontalOffset = 5f
            )
        }

        is FluctuationUiState.Error -> {

        }
    }

}
