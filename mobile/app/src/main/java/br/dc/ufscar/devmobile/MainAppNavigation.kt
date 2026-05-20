package br.dc.ufscar.devmobile

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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

    val context = LocalContext.current
    var hasPermission by remember { mutableStateOf(false) }

    val launcherLocation = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
    }

    LaunchedEffect(Unit) {
        launcherLocation.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

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
                    hasPermission = hasPermission,
                    onRestaurantClick = { storeId ->
                        navController.navigate(Routes.restaurantHome(storeId = storeId))
                    },
                    onCategoryClick = { categoryName ->
                        navController.navigate(Routes.searchResult(categoryName))
                    },
                    onFilterClick = {
                        navController.navigate(Routes.filters(category = null))
                    }
                )
            }
            composable(
                route = Routes.filters,
                arguments = listOf(navArgument("category") { nullable = true; defaultValue = null })
            ) { backStackEntry ->
                val category = backStackEntry.arguments?.getString("category")
                FiltersScreen(
                    category = category ?: "",
                    onApplyingFilterClick = { price, distance, minRating, minReviews, maxReviews, category ->
                        navController.navigate(
                            Routes.searchResult(
                                price = price,
                                distance = distance,
                                minRating = minRating,
                                minReviews = minReviews,
                                maxReviews = maxReviews,
                                category = category
                            )
                        )
                    }
                )
            }
            composable(
                route = Routes.searchResult,
                arguments = listOf(
                    navArgument("category") { nullable = true; defaultValue = null },
                    navArgument("price") { type = NavType.FloatType; defaultValue = -1f },
                    navArgument("distance") { type = NavType.FloatType; defaultValue = -1f },
                    navArgument("minReviews") { type = NavType.IntType; defaultValue = -1 },
                    navArgument("maxReviews") { type = NavType.IntType; defaultValue = -1 },
                    navArgument("minRating") { type = NavType.IntType; defaultValue = -1 }
                )
            ) { backStackEntry ->
                val category = backStackEntry.arguments?.getString("category")?.takeIf { it != "" }
                val price = backStackEntry.arguments?.getFloat("price")?.takeIf { it != -1f }
                val distance = backStackEntry.arguments?.getFloat("distance")?.takeIf { it != -1f }
                val minReviews = backStackEntry.arguments?.getInt("minReviews")?.takeIf { it != -1 }
                val maxReviews = backStackEntry.arguments?.getInt("maxReviews")?.takeIf { it != -1 }
                val minRating = backStackEntry.arguments?.getInt("minRating")?.takeIf { it != -1 }

                SearchResultScreen(
                    hasPermission = hasPermission,
                    category = category,
                    price = price,
                    distance = distance,
                    minReviews = minReviews,
                    maxReviews = maxReviews,
                    minRating = minRating,
                    onRestaurantClick = { storeId ->
                        navController.navigate(Routes.restaurantHome(storeId = storeId))
                    },
                    onFilterClick = {
                        navController.navigate((Routes.filters(category = category)))
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
