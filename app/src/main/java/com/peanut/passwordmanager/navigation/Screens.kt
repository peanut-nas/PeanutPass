package com.peanut.passwordmanager.navigation

import androidx.navigation.NavHostController
import com.peanut.passwordmanager.util.Action
import com.peanut.passwordmanager.util.Constants.HOME_SCREEN

class Screens(navController: NavHostController) {

    val home: (Action)->Unit = {
        navController.navigate("home/${it.name}"){
            popUpTo(HOME_SCREEN){ inclusive = true }
        }
    }

    val item: (Int)->Unit = {
        navController.navigate("item/${it}")
    }
}