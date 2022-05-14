package com.peanut.passwordmanager.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.peanut.passwordmanager.data.models.Account
import com.peanut.passwordmanager.ui.component.AddFloatActionButton
import com.peanut.passwordmanager.ui.component.HomeTopAppBar
import com.peanut.passwordmanager.ui.component.SmallAccountItem
import com.peanut.passwordmanager.ui.viewmodel.SharedViewModel

@Composable
fun HomeScreen(navigateToItemScreen: (Int) -> Unit, sharedViewModel: SharedViewModel) {
    val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }
    LaunchedEffect(key1 = true) {
        sharedViewModel.getAllAccounts()
    }
    val allAccounts by sharedViewModel.allAccounts.collectAsState()
    Scaffold(
        topBar = { HomeTopAppBar(scrollBehavior = scrollBehavior) },
        content = {
            Column(modifier = Modifier.padding(it)) {
                DetailContent(allAccounts = allAccounts, navigateToItemScreen = navigateToItemScreen)
            }
        },
        floatingActionButton = {
            AddFloatActionButton {
                navigateToItemScreen(-1)
            }
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    )
}

@Composable
fun DetailContent(allAccounts: List<Account>, navigateToItemScreen: (Int) -> Unit){
    LazyColumn {
        items(items = allAccounts, key = { account: Account ->
            account.id
        }) { account ->
            SmallAccountItem(account = account, navigateToItemScreen = navigateToItemScreen)
        }
    }
}