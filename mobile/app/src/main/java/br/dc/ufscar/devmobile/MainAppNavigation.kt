package br.dc.ufscar.devmobile

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
            composable(Routes.home) {
                HomeScreen(onStoreClick = { storeId ->
                    navController.navigate(Routes.restaurantHome(storeId))
                })
            }
            composable(Routes.search) {
                SearchScreen(
                    onRestaurantClick = { storeId ->
                        navController.navigate(Routes.restaurantHome(storeId = storeId))
                    },
                    onCategoryClick = { categoryName ->
                        navController.navigate(Routes.searchResult(categoryName))
                    },
                    onFilterClick = {
                        navController.navigate((Routes.filters))
                    }
                )
            }
            composable(Routes.filters) { FiltersScreen() }
            composable(
                route = Routes.searchResult,
                arguments = listOf(navArgument("category") { type = NavType.StringType })
            ) { backStackEntry ->
                val category = backStackEntry.arguments?.getString("category") ?: ""
                SearchResultScreen(
                    category = category,
                    onRestaurantClick = { storeId ->
                        navController.navigate(Routes.restaurantHome(storeId = storeId))
                    },
                    onFilterClick = {
                        navController.navigate((Routes.filters))
                    }
                )
            }
            composable(
                route = Routes.restaurantHome,
                arguments = listOf(navArgument("storeId") { type = NavType.IntType })
            ) { backStackEntry ->
                val storeId = backStackEntry.arguments?.getInt("storeId") ?: 1
                RestaurantHomeScreen(
                    storeId = storeId,
                    onReserveClick = { navController.navigate(Routes.reserve(storeId)) },
                    onMenuClick = { navController.navigate(Routes.restaurantMenu(storeId)) }
                )
            }
            composable(
                route = Routes.restaurantMenu,
                arguments = listOf(navArgument("storeId") { type = NavType.IntType })
            ) { backStackEntry ->
                val storeId = backStackEntry.arguments?.getInt("storeId") ?: 1
                RestaurantMenuScreen(
                    storeId = storeId,
                    onBackClick = { navController.navigateUp() }
                )
            }
            composable(
                route = Routes.reserve,
                arguments = listOf(navArgument("storeId") { type = NavType.IntType })
            ) { backStackEntry ->
                val storeId = backStackEntry.arguments?.getInt("storeId") ?: 1
                ReservationScreen(
                    storeId = storeId,
                    onBackClick = { navController.navigateUp() },
                    onConfirmClick = { navController.navigate(Routes.reserveConfirmation) }
                )
            }
            composable(Routes.reserveConfirmation) {
                ReserveConfirmationScreen(
                    onBackToHomeClick = {
                        navController.navigate(Routes.home) {
                            popUpTo(Routes.home) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
