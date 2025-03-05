package com.peanut.passwordmanager.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Fingerprint
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.peanut.passwordmanager.R

@Composable
fun AccountContentPlaceholder(text: String, painterId: Int = R.drawable.ic_sad_face) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(modifier = Modifier.size(120.dp), painter = painterResource(id = painterId), contentDescription = "Sad Face Icon", tint = MaterialTheme.colorScheme.surfaceDim)
        Text(text = text, color = MaterialTheme.colorScheme.surfaceDim, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.bodyMedium.fontSize)
    }
}

@Composable
fun AccountContentPlaceholder(text: String, painterIcon: ImageVector, clickable: ()->Unit = {}) {
    Column(modifier = Modifier.fillMaxSize().clickable { clickable() }, verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(modifier = Modifier.size(120.dp), imageVector = painterIcon, contentDescription = "Icon", tint = MaterialTheme.colorScheme.surfaceDim)
        Text(text = text, color = MaterialTheme.colorScheme.surfaceDim, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.bodyMedium.fontSize)
    }
}

@Composable
@Preview(showBackground = true)
fun EmptyAccountContentPreview() {
    AccountContentPlaceholder(stringResource(id = R.string.empty_content))
}