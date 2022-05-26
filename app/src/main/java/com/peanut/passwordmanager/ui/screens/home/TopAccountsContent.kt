package com.peanut.passwordmanager.ui.screens.home

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.peanut.passwordmanager.data.models.Account

@Composable
fun TopAccountsContent(allAccounts: List<Account>){
    LazyRow {
        items(items = allAccounts, key = { account: Account ->
            account.id
        }) { account ->
            LargeAccountItem(account = account)
        }
    }
}