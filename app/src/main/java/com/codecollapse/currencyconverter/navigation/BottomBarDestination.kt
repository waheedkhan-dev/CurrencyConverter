package com.codecollapse.currencyconverter.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.codecollapse.currencyconverter.R
import com.codecollapse.currencyconverter.core.DestinationRoute.CONVERT_SCREEN_ROUTE
import com.codecollapse.currencyconverter.core.DestinationRoute.RATE_SCREEN_ROUTE

enum class BottomBarDestination(
    val route: String,
    @StringRes val title: Int? = null,
    @DrawableRes val unFilledIcon: Int,
    @DrawableRes val filledIcon: Int? = null,
    @DrawableRes val darkModeIcon: Int? = null
) {

    CONVERT(
        route = CONVERT_SCREEN_ROUTE,
        title = R.string.convert,
        unFilledIcon = R.drawable.outline_sync_alt_24,
        filledIcon = R.drawable.outline_sync_alt_24
    ),

    RATE(
        route = RATE_SCREEN_ROUTE,
        title = R.string.rate,
        unFilledIcon = R.drawable.outline_timeline_24,
        filledIcon = R.drawable.outline_timeline_24
    ),

}