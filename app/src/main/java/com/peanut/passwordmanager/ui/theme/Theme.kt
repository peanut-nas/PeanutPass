package com.peanut.passwordmanager.ui.theme

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun PasswordManagerTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val context = LocalContext.current
    val view = LocalView.current
    val colorScheme = if (darkTheme) darkColorScheme(secondary = DarkCustomColorScheme.secondary) else lightColorScheme(secondary = LightCustomColorScheme.secondary)
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setStatusBarColor(Color.Transparent)
        WindowCompat.getInsetsController(context.findActivity().window, view)
            .isAppearanceLightStatusBars = !darkTheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

private tailrec fun Context.findActivity(): Activity =
    when (this) {
        is Activity -> this
        is ContextWrapper -> this.baseContext.findActivity()
        else -> throw IllegalArgumentException("Could not find activity!")
    }

