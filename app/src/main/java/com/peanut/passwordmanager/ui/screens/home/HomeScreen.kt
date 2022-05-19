package com.peanut.passwordmanager.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import com.peanut.passwordmanager.R
import com.peanut.passwordmanager.data.models.Account
import com.peanut.passwordmanager.ui.viewmodel.SharedViewModel
import com.peanut.passwordmanager.util.RequestState

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
            Column(modifier = Modifier.padding(it).fillMaxSize()) {
                if (allAccounts is RequestState.Success) {
                    if ((allAccounts as RequestState.Success<List<Account>>).data.isEmpty())
                        AccountContentPlaceholder(stringResource(id = R.string.empty_content))
                    else {
                        TopAccountsContent(allAccounts = (allAccounts as RequestState.Success<List<Account>>).data, navigateToItemScreen = navigateToItemScreen)
                        AllAccountsContent(allAccounts = (allAccounts as RequestState.Success<List<Account>>).data, navigateToItemScreen = navigateToItemScreen, sharedViewModel = sharedViewModel)
                    }
                }else if(allAccounts is RequestState.Error){
                    AccountContentPlaceholder((allAccounts as RequestState.Error).error.localizedMessage?:"出现未知错误")
                }
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