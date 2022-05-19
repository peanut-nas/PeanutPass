package com.peanut.passwordmanager.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peanut.passwordmanager.ui.theme.AccountIconBackground


@Composable
fun AccountIcon(accountName: String, icon: Painter? = null, width: Dp = 46.dp, fontSize: TextUnit = 24.sp) {
    Surface(
        modifier = Modifier
            .padding(12.dp)
            .width(width), shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp)), color = AccountIconBackground.copy(0.5f)
    ) {
        if (icon != null)
            Icon(painter = icon, contentDescription = "Account Icon Background", tint = Color.Unspecified)
        else if (accountName.isNotEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = accountName[0].toString(), fontSize = fontSize)
            }
        }
    }
}

