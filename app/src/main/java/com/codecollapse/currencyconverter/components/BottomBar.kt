package com.codecollapse.currencyconverter.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.codecollapse.currencyconverter.R
import com.codecollapse.currencyconverter.navigation.BottomBarDestination


@Composable
fun BottomBar(
    navController: NavHostController,
    currentDestination: NavDestination?
) {

    val screens = listOf(
        BottomBarDestination.CONVERT,
        BottomBarDestination.RATE
    )
    /*  val navBackStackEntry by navController.currentBackStackEntryAsState()
      val currentDest = navBackStackEntry?.destination*/

    val bottomBarDestination = screens.any { it.route == currentDestination?.route }
    if (bottomBarDestination) {
        NavigationBar(
            containerColor = colorResource(id = R.color.light_green)
        ) {
            screens.forEach { screen ->
                BottomItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
        }
    }
}


@Composable
fun RowScope.BottomItem(
    screen: BottomBarDestination,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    val icon: Int = screen.unFilledIcon
    NavigationBarItem(
        label = {
            screen.title?.let {
                Text(
                    stringResource(id = it), style = TextStyle(
                        fontFamily = FontFamily(
                            Font(
                                R.font.montserrat_regular,
                                weight = FontWeight.Medium
                            )
                        )
                    )
                )
            }
        },
        icon = {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null
            )
        },
        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
        // unselectedContentColor = LocalContentColor.current.copy(alpha = ContentAlpha.disabled),
        onClick = {
            navController.navigate(screen.route) {
                // Pop up to the start destination of the graph to
                // avoid building up a large stack of destinations
                // on the back stack as users select items
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                // Avoid multiple copies of the same destination when
                // reselecting the same item
                launchSingleTop = true
                // Restore state when reselecting a previously selected item
                restoreState = true
            }
            /* navController.navigate(screen.route) {
                 popUpTo(navController.graph.findStartDestination().id)
                 launchSingleTop = true
             }*/
        }
    )
}
