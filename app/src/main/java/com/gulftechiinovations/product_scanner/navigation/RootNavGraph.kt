package com.gulftechiinovations.product_scanner.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gulftechiinovations.product_scanner.screens.home_screen.HomeScreen
import com.gulftechiinovations.product_scanner.screens.license_activation_screen.LicenseActivationScreen
import com.gulftechiinovations.product_scanner.screens.set_base_url_screen.SetBaseUrlScreen
import com.gulftechiinovations.product_scanner.screens.splash_screen.SplashScreen

@Composable
fun RootNavGraph(
    navHostController: NavHostController,
    hideKeyboard: () -> Unit,
) {
    NavHost(
        navController = navHostController,
        startDestination = RootNavScreens.SplashScreen.route
    ) {

        composable(RootNavScreens.SplashScreen.route) {

            SplashScreen(navHostController = navHostController)
        }

        composable(RootNavScreens.HomeScreen.route) {
            HomeScreen(
                hideKeyboard = hideKeyboard,
            )
        }

        composable(RootNavScreens.SetBaseUrlScreen.route) {
            SetBaseUrlScreen(navHostController = navHostController, hideKeyboard = hideKeyboard)
        }

        composable(RootNavScreens.LicenseActivationScreen.route){
            LicenseActivationScreen(
                navHostController = navHostController,
                hideKeyboard = hideKeyboard
            )
        }


    }

}