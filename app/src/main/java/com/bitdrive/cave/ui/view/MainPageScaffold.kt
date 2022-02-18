package com.bitdrive.cave.ui.view

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bitdrive.cave.NavigationBarItems
import com.bitdrive.cave.ui.theme.CaveTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainPageScaffold() {
    val navController = rememberNavController()
    val context = LocalContext.current
    var isFabEnabled by remember {
        mutableStateOf(true)
    }
    navController.addOnDestinationChangedListener { _, destination, _ ->
        isFabEnabled = when (destination.route) {
            NavigationBarItems.Reminders.route -> true
            NavigationBarItems.Shop.route -> true
            else -> false
        }
    }
    CaveTheme {
        val scaffoldState = rememberScaffoldState()
        val modalBottomState =
            rememberModalBottomSheetState(ModalBottomSheetValue.Hidden, skipHalfExpanded = true)
        val scope = rememberCoroutineScope()

        ModalBottomSheetLayout(
            sheetState = modalBottomState,
            sheetContent = {
                Column {
                    Row(
                        Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            Modifier
                                .size(48.dp)
                                .clickable { scope.launch { modalBottomState.hide() } },
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.Close,
                                null,
                                Modifier
                                    .size(24.dp),
                                colors.onSurface
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Add alarm")
                            Text("Alarm in 23 hours 59 minutes")
                        }
                        Column(
                            Modifier
                                .size(48.dp)
                                .clickable { scope.launch { modalBottomState.hide() } },
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.Check,
                                null,
                                Modifier
                                    .size(24.dp),
                                colors.onSurface
                            )
                        }
                    }
                    ListItem(
                        text = { Text("14:23") },
                        overlineText = { Text("Time") },
                        trailing = {
                            Icon(
                                Icons.Default.KeyboardArrowRight,
                                null,
                                tint = colors.onSurface
                            )
                        },
                        modifier = Modifier.clickable { }
                    )
                    ListItem(
                        text = { Text("Ringtone") },
                        secondaryText = { Text("Default Ringtone") },
                        trailing = {
                            Icon(
                                Icons.Default.KeyboardArrowRight,
                                null,
                                tint = colors.onSurface
                            )
                        },
                        modifier = Modifier.clickable { }
                    )
                    ListItem(
                        text = { Text("Repeat") },
                        secondaryText = { Text("Once") },
                        trailing = {
                            Icon(
                                Icons.Default.KeyboardArrowRight,
                                null,
                                tint = colors.onSurface
                            )
                        },
                        modifier = Modifier.clickable { }
                    )
                    var vibrate by remember { mutableStateOf(true) }
                    ListItem(
                        text = { Text("Vibrate when alarm sounds") },
                        trailing = {
                            Switch(
                                checked = vibrate,
                                onCheckedChange = { vibrate = it }
                            )
                        },
                        modifier = Modifier.clickable { vibrate = !vibrate }
                    )
                    var delete by remember { mutableStateOf(false) }
                    ListItem(
                        text = { Text("Delete after goes off") },
                        trailing = {
                            Switch(
                                checked = delete,
                                onCheckedChange = { delete = it }
                            )
                        },
                        modifier = Modifier.clickable { delete = !delete }
                    )
                    ListItem(
                        text = { Text("Label") },
                        trailing = {
                            Icon(
                                Icons.Default.KeyboardArrowRight,
                                null,
                                tint = colors.onSurface
                            )
                        },
                        modifier = Modifier.clickable { }
                    )
                }
            }) {
            Scaffold(
                scaffoldState = scaffoldState,
                bottomBar = {
                    val items = listOf(
                        NavigationBarItems.Reminders,
                        NavigationBarItems.Shop,
                        NavigationBarItems.LifeHacks,
                        NavigationBarItems.Profile
                    )

                    CompositionLocalProvider(LocalRippleTheme provides ClearRippleTheme) {
                        NavigationBar(
                            containerColor = colors.surface,
                        ) {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination
                            items.forEachIndexed { index, item ->
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
                },
                floatingActionButton = {
                    if (isFabEnabled) {
                        FloatingActionButton(
                            backgroundColor = colors.primary,
                            contentColor = colors.onPrimary,
                            content = { Icon(Icons.Filled.Add, null, tint = colors.onPrimary) },
                            onClick = {
                                when (navController.currentDestination?.route) {
                                    NavigationBarItems.Reminders.route -> {
                                        scope.launch { modalBottomState.show() }
                                    }
                                    NavigationBarItems.Shop.route -> {
                                        Toast.makeText(context, "Shop", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            },
                            shape = RoundedCornerShape(16.dp),
                        )
                    }
                },
                floatingActionButtonPosition = FabPosition.Center,
                isFloatingActionButtonDocked = false,
                content = { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = NavigationBarItems.Reminders.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(NavigationBarItems.Reminders.route) { RemindersView(navController) }
                        composable(NavigationBarItems.Shop.route) { ShopView() }
                        composable(NavigationBarItems.LifeHacks.route) { LifeHacksView() }
                        composable(NavigationBarItems.Profile.route) { ProfileView() }
                    }
                }
            )
        }
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