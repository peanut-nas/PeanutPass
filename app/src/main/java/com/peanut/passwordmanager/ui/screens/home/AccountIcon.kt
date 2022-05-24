package com.peanut.passwordmanager.ui.screens.home

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peanut.passwordmanager.ui.theme.AccountIconBackground
import java.io.File


@Composable
fun AccountIcon(accountName: String, icon: String = "", width: Dp = 46.dp, fontSize: TextUnit = 24.sp) {
    val context = LocalContext.current
    val iconFolder = context.filesDir.absolutePath+"/"
    Surface(
        modifier = Modifier
            .padding(start = 0.dp, top = 12.dp, bottom = 12.dp, end = 12.dp)
            .width(width), shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp)), color = AccountIconBackground.copy(0.5f)
    ) {
        if (icon.isNotEmpty()) {
            val iconUri = Uri.fromFile(File(iconFolder+icon))
            val bitmap = remember { mutableStateOf<Bitmap?>(null) }
            if (Build.VERSION.SDK_INT < 28) {
                bitmap.value = MediaStore.Images.Media.getBitmap(context.contentResolver, iconUri)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, iconUri)
                bitmap.value = ImageDecoder.decodeBitmap(source)
            }
            bitmap.value?.let {
                Icon(bitmap = it.asImageBitmap(), contentDescription = "Account Logo Icon", tint = Color.Unspecified, modifier = Modifier.padding(10.dp))
            }
        }
        else if (accountName.isNotEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = accountName[0].toString(), fontSize = fontSize)
            }
        }
    }
}

