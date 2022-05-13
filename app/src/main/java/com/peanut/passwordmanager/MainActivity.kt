package com.peanut.passwordmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.peanut.passwordmanager.navigation.SetupNavigation
import com.peanut.passwordmanager.ui.component.BottomSheetPasswordGenerator
import com.peanut.passwordmanager.ui.theme.PasswordManagerTheme


class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SettingManager.init(this)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            PasswordManagerTheme {
                ProvideWindowInsets {
                    navController = rememberNavController()
                    SetupNavigation(navController = navController)
                }
            }
        }
    }
}