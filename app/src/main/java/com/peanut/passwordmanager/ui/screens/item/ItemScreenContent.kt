package com.peanut.passwordmanager.ui.screens.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.peanut.passwordmanager.R
import com.peanut.passwordmanager.ui.component.PasswordText
import com.peanut.passwordmanager.util.*

@Composable
fun ItemScreenContent(
    title: String,
    account: String,
    icon: String,
    accountType: AccountType,
    password: String,
    onTitleChanged: (String) -> Unit,
    onAccountChanged: (String) -> Unit,
    onIconChanged: (String) -> Unit,
    onAccountTypeChanged: (AccountType) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onOpenBottomSheet: () -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            SelectableLogoIcon(accountName = title, icon = icon) { newIconUri ->
                if (newIconUri != null) {
                    //复制到私有目录，并更新文件名
                    val newIconName = generatePassword(length = 20, useLetter = true, useUpperLetter = true).clearType()
                    AdditionalFunctions.copyFile(newIconUri, context.filesDir.absolutePath + "/" + newIconName, context)
                    onIconChanged(newIconName)
                }
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        KeyAndInputValue(key = stringResource(id = R.string.platform_name), value = title, onValueChange = onTitleChanged)
        KeyAndInputValue(key = stringResource(id = R.string.account_name), value = account, onValueChange = onAccountChanged)
        KeyAndInputValue(
            key = stringResource(id = R.string.account_type),
            value = stringResource(id = accountType.typeNameStringResourceId),
            onValueChange = onAccountChanged,
            readOnly = true,
            trailingIcon = { AccountTypeDropDown(onAccountTypeSelected = onAccountTypeChanged) }
        )
        KeyAndInputValue(
            key = stringResource(id = R.string.password),
            value = password,
            onValueChange = onPasswordChanged,
            customInputValue = {
                Box(modifier = Modifier.fillMaxWidth().clickable { onOpenBottomSheet() }, contentAlignment = Alignment.Center){
                    PasswordText(password = guessTypeFromString(password), paddingValues = PaddingValues(16.dp))
                }
            }
        )
    }
}

@Composable
@Preview(showBackground = true)
fun Preview(){
    KeyAndInputValue(key = stringResource(id = R.string.platform_name), value = "", onValueChange = {})
}