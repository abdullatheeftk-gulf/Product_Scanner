package com.gulftechiinovations.product_scanner.navigation

sealed class RootNavScreens(val route:String) {
    data object  SplashScreen:RootNavScreens("splash_screen")
    data object SetBaseUrlScreen:RootNavScreens("set_base_url_screen")
    data object LicenseActivationScreen:RootNavScreens("license_activation_screen")
    data object HomeScreen:RootNavScreens("home_screen")
}