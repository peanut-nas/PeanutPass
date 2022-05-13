package com.peanut.passwordmanager.navigation.destinations

import androidx.navigation.compose.composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.peanut.passwordmanager.util.Action
import com.peanut.passwordmanager.util.Constants.ITEM_SCREEN


fun NavGraphBuilder.itemComposable(navigateToHomeScreen: (Action) -> Unit){
    composable(
        route= ITEM_SCREEN,
        arguments = listOf(navArgument("itemId"){
            type = NavType.IntType
        })
    ){

    }
}