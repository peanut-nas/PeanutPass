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
    Box(modifier = Modifier
        .height(94.dp)
        .clickable { navigateToItemScreen(account.id) }) {
        AccountIcon(accountName = account.title, icon = account.icon, width = 70.dp, fontSize = 36.sp)
    }
}

@Composable
@Preview(showBackground = true)
fun LargeAccountItemPreview() {
    LargeAccountItem(Account(0, "Microsoft", "", "panrunqiu@outlook.com", "abc********def", AccountType.Email)) {}
}
