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
import com.google.accompanist.insets.ProvideWindowInsets
import com.peanut.passwordmanager.ui.component.BottomSheetPasswordGenerator
import com.peanut.passwordmanager.ui.theme.PasswordManagerTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SettingManager.init(this)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            PasswordManagerTheme {
                ProvideWindowInsets {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        var password by remember {
                            mutableStateOf(
                                listOf("点我生成密码" to 1)
                            )
                        }
                        BottomSheetPasswordGenerator { generated ->
                            password = generated
                        }
                    }
                }
            }
        }
    }
}