package com.peanut.passwordmanager.navigation.destinations

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.navigation.compose.composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.peanut.passwordmanager.ui.screens.home.HomeScreen
import com.peanut.passwordmanager.ui.viewmodel.SharedViewModel
import com.peanut.passwordmanager.util.Action
import com.peanut.passwordmanager.util.Constants.HOME_SCREEN
import com.peanut.passwordmanager.util.toAction


fun NavGraphBuilder.homeComposable(navigateToItemScreen: (Int) -> Unit, sharedViewModel: SharedViewModel){
    composable(
        route=HOME_SCREEN,
        arguments = listOf(navArgument("action"){
            type = NavType.StringType
        })
    ){ navBackStackEntry->
        val action: Action = navBackStackEntry.arguments?.getString("action").toAction()
        val databaseAction by sharedViewModel.action
        LaunchedEffect(key1 = action){
            sharedViewModel.action.value = action
        }
        HomeScreen(action = databaseAction, navigateToItemScreen = navigateToItemScreen, sharedViewModel = sharedViewModel)
    }
}