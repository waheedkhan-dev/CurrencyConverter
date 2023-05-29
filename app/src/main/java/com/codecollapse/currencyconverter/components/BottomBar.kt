package com.codecollapse.currencyconverter.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import com.codecollapse.currencyconverter.navigation.BottomBarDestination


@Composable
fun BottomBar(
    navController: NavHostController,
    currentDestination: NavDestination?,
    isDarkTheme: Boolean
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
            containerColor = MaterialTheme.colorScheme.primaryContainer
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
                Text(stringResource(id = it))
            }
        },
        icon = {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null
            )
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } ?: false,
        // unselectedContentColor = LocalContentColor.current.copy(alpha = ContentAlpha.disabled),
        onClick = {
            screen.route.let {
                navController.navigate(it) {
                    popUpTo(screen.route) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
            /* navController.navigate(screen.route) {
                 popUpTo(navController.graph.findStartDestination().id)
                 launchSingleTop = true
             }*/
        }
    )
}
