package com.peanut.passwordmanager.navigation.destinations

import androidx.navigation.compose.composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.peanut.passwordmanager.ui.screens.HomeScreen
import com.peanut.passwordmanager.ui.viewmodel.SharedViewModel
import com.peanut.passwordmanager.util.Constants.HOME_SCREEN


fun NavGraphBuilder.homeComposable(navigateToItemScreen: (Int) -> Unit, sharedViewModel: SharedViewModel){
    composable(
        route=HOME_SCREEN,
        arguments = listOf(navArgument("action"){
            type = NavType.StringType
        })
    ){
        HomeScreen(navigateToItemScreen = navigateToItemScreen, sharedViewModel = sharedViewModel)
    }
}