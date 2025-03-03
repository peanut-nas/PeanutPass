package com.peanut.passwordmanager.navigation.destinations

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.peanut.passwordmanager.ui.screens.item.ItemScreen
import com.peanut.passwordmanager.ui.viewmodel.SharedViewModel
import com.peanut.passwordmanager.util.Action
import com.peanut.passwordmanager.util.Constants.ITEM_SCREEN


fun NavGraphBuilder.itemComposable(navigateToHomeScreen: (Action) -> Unit, sharedViewModel: SharedViewModel){
    composable(
        route= ITEM_SCREEN,
        arguments = listOf(navArgument("itemId"){
            type = NavType.IntType
        })
    ){
        val accountId = it.arguments!!.getInt("itemId")
        LaunchedEffect(key1 = accountId) {
            sharedViewModel.getSelectedAccount(accountId)
        }
        val selectedAccount by sharedViewModel.selectedAccount.collectAsState()

        LaunchedEffect(key1 = selectedAccount){
            sharedViewModel.updateAccountFields(selectedAccount = selectedAccount)
        }

        ItemScreen(navigateToHomeScreen = navigateToHomeScreen, selectedAccount = selectedAccount, sharedViewModel = sharedViewModel)
    }
}