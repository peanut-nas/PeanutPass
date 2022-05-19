package com.peanut.passwordmanager.ui.screens

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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peanut.passwordmanager.R
import com.peanut.passwordmanager.data.models.Account
import com.peanut.passwordmanager.ui.component.AccountTypeDropDown
import com.peanut.passwordmanager.ui.component.BottomSheetPasswordGenerator
import com.peanut.passwordmanager.ui.component.ItemTopAppBar
import com.peanut.passwordmanager.ui.theme.AccountIconBackground
import com.peanut.passwordmanager.util.AccountType
import com.peanut.passwordmanager.util.Action
import com.peanut.passwordmanager.util.AdditionalFunctions.copyFile
import com.peanut.passwordmanager.util.clearType
import com.peanut.passwordmanager.util.generatePassword
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ItemScreen(navigateToHomeScreen: (Action) -> Unit, selectedAccount: Account?){
    val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }
    Scaffold(
        topBar = { ItemTopAppBar(scrollBehavior = scrollBehavior, navigateToHomeScreen = navigateToHomeScreen, selectedAccount = selectedAccount) },
        content = {
            Surface(modifier = Modifier.padding(it)) {
                var password by remember { mutableStateOf(listOf("点我生成密码" to 0)) }
                val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
                val coroutineScope = rememberCoroutineScope()
                BottomSheetPasswordGenerator(sheetState, coroutineScope, content = {
                    ItemScreenContent(password, account = selectedAccount) {
                        coroutineScope.launch { sheetState.show() }
                    }
                }) { generated ->
                    password = generated
                }
            }
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    )
}

@Composable
fun ItemScreenContent(password: List<Pair<String, Int>>, account: Account?, onOpenBottomSheet: () -> Unit){
    var iconName by remember { mutableStateOf(account?.icon)}
    val context = LocalContext.current
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
            SelectableLogoIcon(accountName = iconName, icon = iconName){ newIconUri ->
                if (newIconUri != null){
                    //复制到私有目录，并更新文件名
                    val newIconName = generatePassword(length = 20, useLetter = true, useUpperLetter = true).clearType()
                    copyFile(newIconUri, context.filesDir.absolutePath+"/"+newIconName, context)
                    iconName = newIconName
                }
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "${stringResource(id = R.string.platform_name)}: ", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = "", onValueChange = {}, label = { Text(text = "Account Name")})
        }
        OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = "", onValueChange = {}, label = { Text(text = "Account")})
        AccountTypeDropDown(accountType = AccountType.Email, onAccountTypeSelected = {})
    }
}

@Composable
fun SelectableLogoIcon(accountName: String? = null, icon: String? = null, onIconSelected: (Uri?) -> Unit){
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
        if (icon != null && icon.isNotEmpty()) {
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
        }else if (accountName != null && accountName.isNotEmpty()) {
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