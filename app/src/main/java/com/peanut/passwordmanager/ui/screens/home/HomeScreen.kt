package com.peanut.passwordmanager.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.peanut.passwordmanager.R
import com.peanut.passwordmanager.data.models.Account
import com.peanut.passwordmanager.ui.viewmodel.SharedViewModel
import com.peanut.passwordmanager.util.AccountSortStrategy
import com.peanut.passwordmanager.util.Action
import com.peanut.passwordmanager.util.RequestState
import com.peanut.passwordmanager.util.TopAppBarState

@Composable
fun HomeScreen(action: Action, navigateToItemScreen: (Int) -> Unit, sharedViewModel: SharedViewModel) {
    LaunchedEffect(key1 = true) {
        sharedViewModel.getAllAccounts()
        sharedViewModel.readSortState()
    }

    LaunchedEffect(key1 = action) {
        sharedViewModel.handleDatabaseActions(action = action)
    }

    val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }
    val topAppBarState by sharedViewModel.topAppBarState
    val snackbarHostState = remember { SnackbarHostState() }
    val sortState by sharedViewModel.sortState.collectAsState()
    val lruAccounts by sharedViewModel.lastRecentUsedAccounts.collectAsState()
    val lfuAccounts by sharedViewModel.lastFrequentUsedAccounts.collectAsState()
    val lcAccounts by sharedViewModel.allAccounts.collectAsState()
    val searchedAccounts by sharedViewModel.searchedAccounts.collectAsState()

    DisplaySnackBar(snackbarHostState = snackbarHostState, onComplete = { sharedViewModel.action.value = it }, action = action,
        accountTitle = sharedViewModel.title.value) {
        sharedViewModel.action.value = it
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { HomeTopAppBar(topAppBarState, sharedViewModel, scrollBehavior) },
        content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .padding(horizontal = 16.dp)
                    .fillMaxSize()
            ) {
                if (sortState is RequestState.Success){
                    when{
                        topAppBarState == TopAppBarState.TRIGGERED -> {
                            if (searchedAccounts is RequestState.Success)
                                HomeSearchContent(
                                    searchedAccounts = (searchedAccounts as RequestState.Success<List<Account>>).data,
                                    navigateToItemScreen = navigateToItemScreen, sharedViewModel = sharedViewModel
                                )
                        }
                        (sortState as RequestState.Success<AccountSortStrategy>).data == AccountSortStrategy.LastCreated -> {
                            if (lcAccounts is RequestState.Success) {
                                HomeContent(
                                    allAccounts = (lcAccounts as RequestState.Success<List<Account>>).data,
                                    allAccountSortStrategy = AccountSortStrategy.LastCreated,
                                    topAccounts = (lcAccounts as RequestState.Success<List<Account>>).data, sharedViewModel, navigateToItemScreen = navigateToItemScreen
                                )
                            } else if (lcAccounts is RequestState.Error) {
                                AccountContentPlaceholder("读取账户失败，" + ((lcAccounts as RequestState.Error).error.localizedMessage ?: "未知错误"))
                            }
                        }
                        (sortState as RequestState.Success<AccountSortStrategy>).data == AccountSortStrategy.LastRecentUsed -> {
                            if (lcAccounts is RequestState.Success) {
                                HomeContent(
                                    allAccounts = lruAccounts,
                                    allAccountSortStrategy = AccountSortStrategy.LastRecentUsed,
                                    topAccounts = (lcAccounts as RequestState.Success<List<Account>>).data, sharedViewModel, navigateToItemScreen = navigateToItemScreen
                                )
                            }
                        }
                        (sortState as RequestState.Success<AccountSortStrategy>).data == AccountSortStrategy.LastFrequentUsed -> {
                            if (lcAccounts is RequestState.Success) {
                                HomeContent(
                                    allAccounts = lfuAccounts,
                                    allAccountSortStrategy = AccountSortStrategy.LastFrequentUsed,
                                    topAccounts = (lcAccounts as RequestState.Success<List<Account>>).data, sharedViewModel, navigateToItemScreen = navigateToItemScreen
                                )
                            }
                        }
                    }
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

@Composable
fun HomeContent(allAccounts: List<Account>, allAccountSortStrategy: AccountSortStrategy, topAccounts: List<Account>, sharedViewModel: SharedViewModel, navigateToItemScreen: (Int) -> Unit) {
    if (allAccounts.isEmpty())
        AccountContentPlaceholder(stringResource(id = R.string.empty_content))
    else {
        SelectableTitle(title = stringResource(id = AccountSortStrategy.LastRecentUsed.sortStrategyStringResourceId),
            isSelectable = false, items = emptyArray<String>(), onSelected = {}, performText = { it })
        TopAccountsContent(allAccounts = topAccounts, navigateToItemScreen = navigateToItemScreen)
        val context = LocalContext.current
        SelectableTitle(
            title = stringResource(id = allAccountSortStrategy.sortStrategyStringResourceId), isSelectable = true,
            items = arrayOf(
                AccountSortStrategy.LastRecentUsed,
                AccountSortStrategy.LastFrequentUsed,
                AccountSortStrategy.LastCreated,
            ), performText = {
                context.resources.getString(it.sortStrategyStringResourceId)
            }, onSelected = {
                sharedViewModel.persistSortState(it)
            })
        AllAccountsContent(allAccounts = allAccounts, navigateToItemScreen = navigateToItemScreen, sharedViewModel = sharedViewModel)
    }
}

@Composable
fun HomeSearchContent(searchedAccounts: List<Account>, sharedViewModel: SharedViewModel, navigateToItemScreen: (Int) -> Unit) {
    if (searchedAccounts.isEmpty())
        AccountContentPlaceholder(stringResource(id = R.string.empty_content))
    else {
        AllAccountsContent(allAccounts = searchedAccounts, navigateToItemScreen = navigateToItemScreen, sharedViewModel = sharedViewModel)
    }
}
