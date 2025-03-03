package com.peanut.passwordmanager.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.peanut.passwordmanager.R
import kotlinx.coroutines.delay

@Deprecated("Use System SplashScreen instead")
@Composable
fun SplashScreen(navigateToHomeScreen: () -> Unit) {
    LaunchedEffect(key1 = true, block = {
        delay(300)
        navigateToHomeScreen()
    })
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background), contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.size(100.dp),
            painter = painterResource(id = getLogo()), contentDescription = null
        )
    }
}

@Composable
fun getLogo(): Int {
    return if (isSystemInDarkTheme())
        R.drawable.ic_splash_logo_dark
    else R.drawable.ic_splash_logo_light
}

@Composable
@Preview
fun SplashScreenPreview() {
    SplashScreen {}
}