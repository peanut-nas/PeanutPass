package com.peanut.passwordmanager.ui.screens.home

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.peanut.passwordmanager.data.models.Account

@Composable
fun TopAccountsContent(allAccounts: List<Account>, navigateToItemScreen: (Int) -> Unit){
    LazyRow {
        items(items = allAccounts, key = { account: Account ->
            account.id
        }) { account ->
            LargeAccountItem(account = account, navigateToItemScreen = navigateToItemScreen)
        }
    }
}