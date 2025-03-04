package com.peanut.passwordmanager

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.autofill.AutofillManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.peanut.passwordmanager.navigation.SetupNavigation
import com.peanut.passwordmanager.ui.theme.PasswordManagerTheme
import com.peanut.passwordmanager.ui.viewmodel.SharedViewModel
import com.peanut.passwordmanager.util.SettingManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SettingManager.init(this)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        sharedViewModel.feedData()
        setContent {
            PasswordManagerTheme {
                ProvideWindowInsets {
                    navController = rememberNavController()
                    SetupNavigation(navController = navController, sharedViewModel = sharedViewModel)
                }
            }
        }
        if (!checkAutofillStatus()) {
            lifecycleScope.launch {
                sharedViewModel.shouldShow(Settings.ACTION_REQUEST_SET_AUTOFILL_SERVICE) {
                    val intent = Intent(Settings.ACTION_REQUEST_SET_AUTOFILL_SERVICE).apply {
                        data = Uri.parse("package:$packageName")
                    }
                    startActivity(intent)
                    return@shouldShow true
                }
            }
        }
    }

    private fun checkAutofillStatus(): Boolean {
        val autofillManager = getSystemService(AutofillManager::class.java)
        if (!autofillManager.isAutofillSupported) return true
        return autofillManager.hasEnabledAutofillServices()
    }

}