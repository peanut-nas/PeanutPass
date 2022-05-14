package com.peanut.passwordmanager.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peanut.passwordmanager.ui.theme.AccountIconBackground

@Composable
fun SmallAccountItem() {
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(70.dp)
        .clickable { }) {
        AccountIcon(accountName = "微软")
        Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxHeight()) {
            Text(
                text = "Dribbble",
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "example@email.com",
                modifier = Modifier.padding(bottom = 12.dp),
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface.copy(ContentAlpha.disabled)),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun LargeAccountItem() {
    Box(modifier = Modifier
        .height(94.dp)
        .clickable { }) {
        AccountIcon(accountName = "微软", width = 70.dp, fontSize = 36.sp)
    }
}

@Composable
fun AccountIcon(accountName: String, icon: Painter? = null, width: Dp = 46.dp, fontSize: TextUnit = 24.sp) {
    Surface(modifier = Modifier
        .padding(12.dp)
        .width(width), shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp)), color = AccountIconBackground.copy(0.5f)) {
        if (icon != null)
            Icon(painter = icon, contentDescription = "Account Icon Background", tint = Color.Unspecified)
        else if (accountName.isNotEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = accountName[0].toString(), fontSize = fontSize)
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun AccountItemPreview() {
    SmallAccountItem()
}

@Composable
@Preview(showBackground = true)
fun LargeAccountItemPreview() {
    LargeAccountItem()
}