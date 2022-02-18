package com.bitdrive.cave

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationBarItems(
    val route: String,
    val title: String,
    val iconSelected: ImageVector,
    val iconUnselected: ImageVector
) {
    object Reminders : NavigationBarItems(
        route = "reminders",
        title = "Reminders",
        iconSelected = Icons.Filled.DateRange,
        iconUnselected = Icons.Outlined.DateRange
    )

    object Shop :
        NavigationBarItems(
            route = "shop",
            title = "Shop",
            iconSelected = Icons.Filled.Menu,
            iconUnselected = Icons.Outlined.Menu
        )

    object LifeHacks : NavigationBarItems(
        route = "life_hacks",
        title = "Life Hacks",
        iconSelected = Icons.Filled.ArrowForward,
        iconUnselected = Icons.Outlined.ArrowForward
    )

    object Profile : NavigationBarItems(
        route = "profile",
        title = "Profile",
        iconSelected = Icons.Filled.Person,
        iconUnselected = Icons.Outlined.Person
    )
}
