package com.peanut.passwordmanager

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.biometric.BiometricPrompt
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Fingerprint
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.peanut.passwordmanager.navigation.SetupNavigation
import com.peanut.passwordmanager.ui.screens.home.AccountContentPlaceholder
import com.peanut.passwordmanager.ui.theme.PasswordManagerTheme
import com.peanut.passwordmanager.ui.viewmodel.SharedViewModel
import com.peanut.passwordmanager.util.SettingManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    private val sharedViewModel: SharedViewModel by viewModels()
    private lateinit var biometricPrompt: BiometricPrompt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SettingManager.init(this)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        biometricPrompt = createBiometric(this)
        sharedViewModel.authorize(biometricPrompt)
        setContent {
            PasswordManagerTheme {
                ProvideWindowInsets {
                    val authorized by sharedViewModel.authorized.collectAsState()
                    if (authorized) {
                        SetupNavigation(navController = rememberNavController(), sharedViewModel = sharedViewModel)
                    } else {
                        AccountContentPlaceholder(stringResource(id = R.string.unlock_device), Icons.Rounded.Fingerprint) {
                            sharedViewModel.authorize(biometricPrompt)
                        }
                    }
                }
            }
        }
    }

    private fun createBiometric(context: FragmentActivity): BiometricPrompt {
        return BiometricPrompt(context, ContextCompat.getMainExecutor(context), object : BiometricPrompt.AuthenticationCallback() {

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(context, "Authentication failed", Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                if (errorCode == BiometricPrompt.ERROR_NO_BIOMETRICS) {
                    Toast.makeText(context, "Not Available on this device", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Authentication error: $errorCode $errString", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                println(result.cryptoObject)
                sharedViewModel.login()
                sharedViewModel.feedData()
                sharedViewModel.enableAutofill(context)
            }
        })
    }
}