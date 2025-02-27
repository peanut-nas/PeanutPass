package com.peanut.passwordmanager.ui.screens.item

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.ContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peanut.passwordmanager.ui.theme.AccountIconBackground
import java.io.File

@Composable
fun SelectableLogoIcon(accountName: String? = null, icon: String? = null, iconPaddingValues: PaddingValues = PaddingValues(0.dp), onIconSelected: (Uri?) -> Unit){
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        onIconSelected(it)
    }
    val context = LocalContext.current
    val iconFolder = context.filesDir.absolutePath+"/"
    Surface(
        modifier = Modifier
            .padding(top = 16.dp)
            .size(100.dp)
            .clickable { launcher.launch("image/*") },
        shape = MaterialTheme.shapes.medium.copy(CornerSize(28.dp)), color = AccountIconBackground.copy(0.5f)
    ) {
        if (!icon.isNullOrEmpty()) {
            val iconUri = Uri.fromFile(File(iconFolder+icon))
            val bitmap = remember { mutableStateOf<Bitmap?>(null) }
            val source = ImageDecoder.createSource(context.contentResolver, iconUri)
            bitmap.value = ImageDecoder.decodeBitmap(source)
            bitmap.value?.let {
                Icon(bitmap = it.asImageBitmap(), contentDescription = "Account Logo Icon", tint = Color.Unspecified, modifier = Modifier.padding(iconPaddingValues))
            }
        }else if (!accountName.isNullOrEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = accountName[0].toString(), fontSize = 60.sp)
            }
        }else{
            Icon(imageVector = Icons.Rounded.Add, contentDescription = "Select Account Logo", modifier = Modifier
                .padding(20.dp)
                .alpha(ContentAlpha.disabled))
        }
    }
}