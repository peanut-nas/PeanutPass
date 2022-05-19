package com.peanut.passwordmanager.ui.screens.item

import androidx.compose.foundation.layout.*
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peanut.passwordmanager.R
import com.peanut.passwordmanager.data.models.Account
import com.peanut.passwordmanager.util.AccountType
import com.peanut.passwordmanager.util.AdditionalFunctions
import com.peanut.passwordmanager.util.clearType
import com.peanut.passwordmanager.util.generatePassword

@Composable
fun ItemScreenContent(title: String,
                      account: String,
                      icon: String,
                      accountType: AccountType,
                      password: String,
                      onTitleChanged: (String) -> Unit,
                      onAccountChanged: (String) -> Unit,
                      onIconChanged: (String) -> Unit,
                      onAccountTypeChanged: (AccountType) -> Unit,
                      onPasswordChanged: (String) -> Unit,
                      onOpenBottomSheet: () -> Unit){
    val context = LocalContext.current
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
            SelectableLogoIcon(accountName = title, icon = icon){ newIconUri ->
                if (newIconUri != null){
                    //复制到私有目录，并更新文件名
                    val newIconName = generatePassword(length = 20, useLetter = true, useUpperLetter = true).clearType()
                    AdditionalFunctions.copyFile(newIconUri, context.filesDir.absolutePath+"/"+newIconName, context)
                    onIconChanged(newIconName)
                }
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "${stringResource(id = R.string.platform_name)}: ", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = "", onValueChange = {}, label = { Text(text = "Account Name") })
        }
        OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = "", onValueChange = {}, label = { Text(text = "Account") })
        AccountTypeDropDown(accountType = AccountType.Email, onAccountTypeSelected = {})
    }
}