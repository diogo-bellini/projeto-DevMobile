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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.dc.ufscar.devmobile.composables.AppBottomNavigation
import br.dc.ufscar.devmobile.entities.bottomNavItems
import androidx.lifecycle.viewmodel.compose.viewModel
import br.dc.ufscar.devmobile.configs.UPeekDatabase
import br.dc.ufscar.devmobile.viewmodels.AuthViewModel
import br.dc.ufscar.devmobile.viewmodels.AuthViewModelFactory
import br.dc.ufscar.devmobile.viewmodels.ProfileViewModel
import br.dc.ufscar.devmobile.viewmodels.ProfileViewModelFactory
import br.dc.ufscar.devmobile.views.*

@Composable
fun MainAppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val db = UPeekDatabase.getInstance(context)
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(db.userDao())
    )
    val profileViewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModelFactory(db.userDao())
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val screensWithoutBottomBar = listOf(
        Routes.register,
        Routes.login,
        Routes.reserveConfirmation
    )

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
            startDestination = Routes.login,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Routes.register) {
                RegisterScreen(
                    viewModel = authViewModel,
                    onRegisterSuccess = {
                        navController.navigate(Routes.home) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onLoginClick = {
                        navController.popBackStack()
                    }
                )
            }
            composable(Routes.login) {
                LoginScreen(
                    viewModel = authViewModel,
                    onLoginSuccess = {
                        navController.navigate(Routes.home) {
                            popUpTo(Routes.login) { inclusive = true }
                        }
                    },
                    onRegisterClick = {
                        navController.navigate(Routes.register)
                    }
                )
            }
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
                    hasPermission = hasPermission,
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
            composable(Routes.profile) {
                ProfileScreen(
                    viewModel = profileViewModel,
                    onBackClick = { navController.navigateUp() },
                    onLogout = {
                        navController.navigate(Routes.login) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}