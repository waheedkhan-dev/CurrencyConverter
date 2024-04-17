package com.codecollapse.currencyconverter.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.codecollapse.currencyconverter.R

enum class BottomBarDestination(
    val route: String,
    @StringRes val title: Int? = null,
    @DrawableRes val unFilledIcon: Int,
    @DrawableRes val filledIcon: Int? = null,
    @DrawableRes val darkModeIcon: Int? = null
) {

    CONVERT(
        route = Destinations.HomeScreen.route,
        title = R.string.convert,
        unFilledIcon = R.drawable.outline_sync_alt_24,
        filledIcon = R.drawable.outline_sync_alt_24
    ),

    RATE(
        route = Destinations.ChartScreen.route,
        title = R.string.chart,
        unFilledIcon = R.drawable.outline_timeline_24,
        filledIcon = R.drawable.outline_timeline_24
    ),

}