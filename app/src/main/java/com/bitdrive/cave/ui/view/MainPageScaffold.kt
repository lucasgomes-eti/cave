package com.bitdrive.cave.ui.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bitdrive.cave.NavigationBarItems
import com.bitdrive.cave.Routes
import com.bitdrive.cave.ui.theme.CaveTheme
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPageScaffold() {
    val navController = rememberNavController()
    CaveTheme {
        val navigationBarItems = listOf(
            NavigationBarItems.Reminders,
            NavigationBarItems.Shop,
            NavigationBarItems.LifeHacks,
            NavigationBarItems.Profile
        )

        val showBottomBar = navController.currentBackStackEntryAsState()
            .value?.destination?.route in navigationBarItems.map { it.route }

        Scaffold(
            bottomBar = {
                if (showBottomBar) {
                    CompositionLocalProvider(LocalRippleTheme provides ClearRippleTheme) {
                        NavigationBar(
                            containerColor = colorScheme.surface,
                        ) {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination
                            navigationBarItems.forEachIndexed { _, item ->
                                val isSelected =
                                    currentDestination?.hierarchy?.any { it.route == item.route } == true //selectedItem == index
                                NavigationBarItem(
                                    icon = {
                                        Icon(
                                            if (isSelected) item.iconSelected else item.iconUnselected,
                                            contentDescription = item.title,
                                        )
                                    },
                                    label = { Text(item.title) },
                                    selected = isSelected,
                                    onClick = {
                                        navController.navigate(item.route) {
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
                                    }
                                )
                            }
                        }
                    }
                }
            },
            content = { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = NavigationBarItems.Reminders.route,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable(NavigationBarItems.Reminders.route) {
                        RemindersView(
                            viewModel = koinViewModel(),
                            navController
                        )
                    }
                    composable(NavigationBarItems.Shop.route) { ShopView() }
                    composable(NavigationBarItems.LifeHacks.route) { LifeHacksView() }
                    composable(NavigationBarItems.Profile.route) { ProfileView() }

                    composable(
                        Routes.newOrEditAlarm.fullPath(null),
                        arguments = listOf(navArgument(Routes.newOrEditAlarm.alarmIdArgument) {
                            type = NavType.StringType
                            nullable = true
                        })
                    ) { NewOrEditAlarm(navController = navController) }
                }
            }
        )

    }
}

object ClearRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor(): Color = Color.Transparent

    @Composable
    override fun rippleAlpha() = RippleAlpha(
        draggedAlpha = 0.0f,
        focusedAlpha = 0.0f,
        hoveredAlpha = 0.0f,
        pressedAlpha = 0.0f,
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CaveTheme {
        MainPageScaffold()
    }
}