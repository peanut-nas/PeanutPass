package com.peanut.passwordmanager.ui.screens.item

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun KeyAndInputValue(key: String, value: String, onValueChange: (String) -> Unit, readOnly: Boolean = false, trailingIcon: @Composable () -> Unit = {}, customInputValue: @Composable (() -> Unit)? = null){
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(text = "$key:", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Spacer(modifier = Modifier.width(12.dp))
        Surface(modifier = Modifier
            .fillMaxWidth(),
            shape = MaterialTheme.shapes.medium.copy(CornerSize(10.dp)),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),) {
            if (customInputValue != null)
                customInputValue()
            else
                TextField(
                    value = value, onValueChange = { onValueChange(it) },
                    readOnly = readOnly,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.onSurface,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    maxLines = 1,
                    trailingIcon = trailingIcon
                )
        }
    }
}