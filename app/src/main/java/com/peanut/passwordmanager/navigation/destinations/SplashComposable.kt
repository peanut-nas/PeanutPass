package com.peanut.passwordmanager.navigation.destinations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.peanut.passwordmanager.ui.screens.splash.SplashScreen
import com.peanut.passwordmanager.util.Constants

fun NavGraphBuilder.splashComposable(navigateToHomeScreen: () -> Unit){
    composable(route= Constants.SPLASH_SCREEN){
        SplashScreen(navigateToHomeScreen)
    }
}