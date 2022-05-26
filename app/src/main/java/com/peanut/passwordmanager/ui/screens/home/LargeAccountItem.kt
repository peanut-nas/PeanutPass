package com.peanut.passwordmanager.ui.screens.home

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peanut.passwordmanager.R
import com.peanut.passwordmanager.data.models.Account
import com.peanut.passwordmanager.util.AccountType
import com.peanut.passwordmanager.util.AdditionalFunctions.copy

@Composable
fun LargeAccountItem(account: Account) {
    val context = LocalContext.current
    val message = stringResource(id = R.string.account_password_copy)
    AccountIcon(
        modifier = Modifier.clickable {
            account.password.copy(context)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        },
        accountName = account.title, icon = account.icon, width = 60.dp, fontSize = 32.sp
    )
}

@Composable
@Preview(showBackground = true)
fun LargeAccountItemPreview() {
    LargeAccountItem(Account(0, "Microsoft", "", "panrunqiu@outlook.com", "abc********def", AccountType.Email))
}
