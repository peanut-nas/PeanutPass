package com.peanut.passwordmanager.navigation

import androidx.navigation.NavHostController
import com.peanut.passwordmanager.util.Action
import com.peanut.passwordmanager.util.Constants
import com.peanut.passwordmanager.util.Constants.HOME_SCREEN
import com.peanut.passwordmanager.util.Constants.ITEM_SCREEN

class Screens(navController: NavHostController) {

    val splash: () -> Unit = {
        navController.navigate(route = "home/${Action.NO_ACTION.name}") {
            popUpTo(Constants.SPLASH_SCREEN){ inclusive = true }
        }
    }

    val home: (Action)->Unit = {
        navController.navigate("home/${it.name}"){
            popUpTo(HOME_SCREEN){ inclusive = true }
        }
    }

    val item: (Int)->Unit = {
        navController.navigate("item/${it}")
    }
}