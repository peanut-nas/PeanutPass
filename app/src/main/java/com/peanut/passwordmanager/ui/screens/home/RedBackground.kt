package com.peanut.passwordmanager.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RedBackground(degrees: Float) {
    Surface(shape = MaterialTheme.shapes.medium) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Red)
                .padding(horizontal = 16.dp), contentAlignment = Alignment.CenterEnd
        ) {
            Icon(modifier = Modifier.rotate(degrees), imageVector = Icons.Rounded.Delete, contentDescription = null, tint = Color.White)
        }
    }
}