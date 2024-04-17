package com.codecollapse.currencyconverter.ui.screens.currency

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codecollapse.currencyconverter.R
import com.codecollapse.currencyconverter.data.model.currency.CommonCurrency
import com.codecollapse.currencyconverter.data.model.currency.Currency

@Composable
fun CountryRoute(
    isChangingCurrency: Boolean,
    defaultSelectedCurrency: Currency,
    list: Array<CommonCurrency>,
    updateCurrency: (String) -> Unit,
    addCurrency: (String, String, String, String) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.card_background_color))
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            modifier = Modifier
                .padding(8.dp)
                .align(alignment = Alignment.CenterHorizontally),
            text = "Select currency", style = TextStyle(
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                color = colorResource(id = R.color.color_header_text)
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
        var searchQuery by rememberSaveable { mutableStateOf("") }
        SearchEditText(onSearchQueryChange = { newQuery ->
            searchQuery = newQuery
            // Perform search operation with newQuery
        })


        val filteredList = list.filter { currency ->
            currency.name.contains(searchQuery, ignoreCase = true) ||
                    currency.code.contains(searchQuery, ignoreCase = true) ||
                    currency.symbol.contains(searchQuery, ignoreCase = true)
        }.sortedByDescending { currency ->
            when {
                currency.name.equals(searchQuery, ignoreCase = true) -> 3 // Exact match on name
                currency.name.contains(searchQuery, ignoreCase = true) -> 2 // Partial match on name
                currency.code.equals(searchQuery, ignoreCase = true) -> 2 // Exact match on code
                currency.code.contains(searchQuery, ignoreCase = true) -> 1 // Partial match on code
                else -> 0
            }
        }

        CountryRow(
            currencyList = filteredList.ifEmpty { list.toList() },
            isChangingCurrency = isChangingCurrency,
            defaultSelectedCurrency = defaultSelectedCurrency,
            updateCurrency = { code ->
                updateCurrency(code)
            },
            addCurrency = { name, code, symbol, isoCode ->
                addCurrency(name, code, symbol, isoCode)
            }
        )
    }

}

@Composable
fun CountryRow(
    currencyList: List<CommonCurrency>,
    isChangingCurrency: Boolean,
    defaultSelectedCurrency: Currency,
    updateCurrency: (String) -> Unit,
    addCurrency: (String, String, String, String) -> Unit
) {
    val listState = rememberLazyListState()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(16.dp)
    ) {

        items(currencyList.size) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    if (isChangingCurrency) {
                        updateCurrency(currencyList[it].code)
                        //  currencyViewModel.updatedBaseCurrency(currencyList[it].code)
                    } else {
                        if (defaultSelectedCurrency.isoCode != currencyList[it].isoCode) {
                            addCurrency(
                                currencyList[it].name, currencyList[it].code,
                                currencyList[it].symbol,
                                currencyList[it].isoCode
                            )

                        }
                    }

                    //  navController.popBackStack(Destinations.HomeScreen.route, inclusive = false)
                }) {
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

            }
        }
    }

}

@Composable
fun SearchEditText(onSearchQueryChange: (String) -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column {
        TextField(
            value = searchQuery,
            onValueChange = { newValue ->
                searchQuery = newValue
                onSearchQueryChange(newValue)
            },
            modifier = Modifier
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        keyboardController?.show()
                    }
                }
                .fillMaxWidth(),
            placeholder = {
                Text(text = "Search")
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboardController?.hide()
                    // Perform search operation here
                }
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon"
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            searchQuery = ""
                            onSearchQueryChange("")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear Icon"
                        )
                    }
                }
            },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}