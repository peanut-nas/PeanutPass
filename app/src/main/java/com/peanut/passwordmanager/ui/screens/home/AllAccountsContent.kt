package com.peanut.passwordmanager.ui.screens.home

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.peanut.passwordmanager.data.models.Account
import com.peanut.passwordmanager.ui.viewmodel.SharedViewModel

@Composable
fun AllAccountsContent(allAccounts: List<Account>, navigateToItemScreen: (Int) -> Unit, sharedViewModel: SharedViewModel){
    LazyColumn {
        items(items = allAccounts, key = { account: Account ->
            account.id
        }) { account ->
            SmallAccountItem(account = account, navigateToItemScreen = navigateToItemScreen, sharedViewModel = sharedViewModel)
        }
    }
}