package com.peanut.passwordmanager.navigation.destinations

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.peanut.passwordmanager.ui.component.BottomSheetPasswordGenerator
import com.peanut.passwordmanager.util.Action
import com.peanut.passwordmanager.util.Constants.ITEM_SCREEN


fun NavGraphBuilder.itemComposable(navigateToHomeScreen: (Action) -> Unit){
    composable(
        route= ITEM_SCREEN,
        arguments = listOf(navArgument("itemId"){
            type = NavType.IntType
        })
    ){
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            var password by remember { mutableStateOf(listOf("点我生成密码" to 1)) }
            BottomSheetPasswordGenerator { generated ->
                password = generated
            }
        }
    }
}