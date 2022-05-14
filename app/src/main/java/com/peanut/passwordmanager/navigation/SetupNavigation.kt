package com.peanut.passwordmanager.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.peanut.passwordmanager.navigation.destinations.homeComposable
import com.peanut.passwordmanager.navigation.destinations.itemComposable
import com.peanut.passwordmanager.ui.viewmodel.SharedViewModel
import com.peanut.passwordmanager.util.Constants.HOME_SCREEN

@Composable
fun SetupNavigation(navController: NavHostController, sharedViewModel: SharedViewModel){
    val screen = remember(navController) {
        Screens(navController = navController)
    }

    NavHost(navController = navController, startDestination = HOME_SCREEN){
        homeComposable(navigateToItemScreen = screen.item, sharedViewModel)
        itemComposable(navigateToHomeScreen = screen.home)
    }
}