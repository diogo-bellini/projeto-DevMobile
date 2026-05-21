package br.dc.ufscar.devmobile.entities

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import br.dc.ufscar.devmobile.Routes
import br.dc.ufscar.devmobile.R

data class BottomNavItem(
    @StringRes val title: Int,
    val icon: ImageVector,
    val route: String
)

val bottomNavItems = listOf(
    BottomNavItem(title = R.string.nav_home, icon = Icons.Default.Home, route = Routes.home),
    BottomNavItem(title = R.string.nav_search, icon = Icons.Default.Search, route = Routes.search),
    BottomNavItem(title = R.string.nav_reservations, icon = Icons.AutoMirrored.Filled.EventNote, route = Routes.reservations),
    BottomNavItem(title = R.string.nav_profile, icon = Icons.Default.Person, route = Routes.profile),
)
