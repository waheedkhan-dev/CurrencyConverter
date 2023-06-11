package com.codecollapse.currencyconverter.utils.previewProvider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.codecollapse.currencyconverter.data.model.currency.Currency

class CurrencyProvider : PreviewParameterProvider<Currency> {
    override val values: Sequence<Currency>
        get() = sequenceOf(
            Currency(
                name = "British Pound",
                symbol = "£",
                date = "2023-05-31",
                rate = 0.003549,
                timestamp = 1685518803,
                amount = 200,
                from = "PKR",
                to = "GBP",
                result = 200.0.times(0.003549),
                isoCode = "GB",
                success = true,
                isFirst = true
            )
        )
}