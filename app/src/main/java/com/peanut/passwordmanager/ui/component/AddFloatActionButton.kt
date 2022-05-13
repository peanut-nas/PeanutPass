package com.peanut.passwordmanager.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.peanut.passwordmanager.R

@Composable
fun AddFloatActionButton(onclick: () -> Unit) {
    FloatingActionButton(onClick = { onclick() }) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = stringResource(id = R.string.add_password),
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}