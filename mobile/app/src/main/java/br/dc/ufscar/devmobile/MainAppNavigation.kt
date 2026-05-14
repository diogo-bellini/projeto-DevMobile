package br.dc.ufscar.devmobile

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.dc.ufscar.devmobile.composables.AppBottomNavigation
import br.dc.ufscar.devmobile.entities.bottomNavItems
import br.dc.ufscar.devmobile.views.*

@Composable
fun MainAppNavigation() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val screensWithoutBottomBar = listOf(
        Routes.register,
        Routes.login,
        Routes.reserveConfirmation
    )

    Scaffold(
        bottomBar = {
            if (currentRoute !in screensWithoutBottomBar) {
                AppBottomNavigation(
                    items = bottomNavItems,
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Routes.home,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Routes.register) { RegisterScreen() }
            composable(Routes.login) { LoginScreen() }
            composable(Routes.home) { HomeScreen() }
            composable(Routes.search) { SearchScreen() }
            composable(Routes.filters) { FiltersScreen() }
            composable(Routes.searchResult) { SearchResultScreen() }
            composable(Routes.restaurantHome) { RestaurantHomeScreen() }
            composable(Routes.restaurantMenu) { RestaurantMenuScreen() }
            composable(Routes.reserve) { ReserveScreen() }
            composable(Routes.reserveConfirmation) { ReserveConfirmationScreen() }
        }
    }
}
