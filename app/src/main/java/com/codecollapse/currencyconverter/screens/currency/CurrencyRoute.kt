package com.codecollapse.currencyconverter.screens.currency

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.codecollapse.currencyconverter.core.ui.currency.CurrencyUiState
import com.codecollapse.currencyconverter.data.model.currency.CommonCurrency

@Composable
fun CountryRoute(
    navController: NavController,
    currencyViewModel: CurrencyViewModel = hiltViewModel()
) {
    val countryUiState by currencyViewModel.currencyUiState.collectAsStateWithLifecycle()

    when (countryUiState) {
        CurrencyUiState.Loading -> {

        }

        is CurrencyUiState.Success -> {
            Column() {
                val state = (countryUiState as CurrencyUiState.Success).countryItem
                var searchQuery by remember { mutableStateOf("") }
                SearchEditText(onSearchQueryChange = { newQuery ->
                    searchQuery = newQuery
                    // Perform search operation with newQuery
                })
                val filteredList = state.filter { currency ->
                    currency.name.contains(searchQuery, ignoreCase = true)
                }
                if(filteredList.isEmpty().not()){
                    CountryRow(currencyList = filteredList)
                }else{
                    CountryRow(currencyList = state.toList())
                }
            }

        }

        CurrencyUiState.Error -> {

        }
    }
}

@Composable
fun CountryRow(currencyList : List<CommonCurrency>) {
    val listState = rememberLazyListState()
    var selectedItem by remember{mutableStateOf( "")}
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(16.dp)
    ) {

        items(currencyList.size) {
            Row(modifier = Modifier.fillMaxWidth().selectable(
                selected = selectedItem == currencyList[it].name, onClick = {
                    selectedItem = currencyList[it].name
                }
            )) {
                if(selectedItem == currencyList[it].name){
                    Text(text = currencyList[it].name, color = Color.Red)
                }else{
                    Text(text = currencyList[it].name)
                }

            }
        }
    }

}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)

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
                    /*  if (focusState == FocusState.Active) {
                          keyboardController?.show()
                      }*/
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
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}