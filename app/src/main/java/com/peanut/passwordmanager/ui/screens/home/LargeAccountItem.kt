package com.peanut.passwordmanager.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peanut.passwordmanager.data.models.Account
import com.peanut.passwordmanager.util.AccountType

@Composable
fun LargeAccountItem(account: Account, navigateToItemScreen: (accountId: Int) -> Unit) {
   AccountIcon(modifier = Modifier.clickable { navigateToItemScreen(account.id) },
       accountName = account.title, icon = account.icon, width = 60.dp, fontSize = 32.sp)
}

@Composable
@Preview(showBackground = true)
fun LargeAccountItemPreview() {
    LargeAccountItem(Account(0, "Microsoft", "", "panrunqiu@outlook.com", "abc********def", AccountType.Email)) {}
}
