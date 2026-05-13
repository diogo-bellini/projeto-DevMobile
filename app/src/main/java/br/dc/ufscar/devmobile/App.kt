package br.dc.ufscar.devmobile

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.dc.ufscar.devmobile.views.FiltersScreen
import br.dc.ufscar.devmobile.views.HomeScreen
import br.dc.ufscar.devmobile.views.LoginScreen
import br.dc.ufscar.devmobile.views.RegisterScreen
import br.dc.ufscar.devmobile.views.ReserveConfirmationScreen
import br.dc.ufscar.devmobile.views.ReserveScreen
import br.dc.ufscar.devmobile.views.RestaurantHomeScreen
import br.dc.ufscar.devmobile.views.RestaurantMenuScreen
import br.dc.ufscar.devmobile.views.SearchResultScreen
import br.dc.ufscar.devmobile.views.SearchScreen

@Composable
fun App(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.register) {
        composable(Routes.register){
            RegisterScreen()
        }
        composable(Routes.login){
            LoginScreen()
        }
        composable(Routes.home){
            HomeScreen()
        }
        composable(Routes.search){
            SearchScreen()
        }
        composable(Routes.filters){
            FiltersScreen()
        }
        composable(Routes.searchResult){
            SearchResultScreen()
        }
        composable(Routes.restaurantHome){
            RestaurantHomeScreen()
        }
        composable(Routes.restaurantMenu){
            RestaurantMenuScreen()
        }
        composable(Routes.reserve){
            ReserveScreen()
        }
        composable(Routes.reserveConfirmation){
            ReserveConfirmationScreen()
        }
    }
}