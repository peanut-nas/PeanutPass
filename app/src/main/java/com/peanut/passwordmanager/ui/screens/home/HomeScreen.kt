package com.peanut.passwordmanager.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(action: Action, navigateToItemScreen: (account: Int) -> Unit, sharedViewModel: SharedViewModel) {

    LaunchedEffect(key1 = action) {
        sharedViewModel.handleDatabaseActions(action = action)
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val topAppBarState by sharedViewModel.topAppBarState
    val snackBarHostState = remember { SnackbarHostState() }
    val topAccounts by sharedViewModel.topAccounts.collectAsState()
    val mainAccount by sharedViewModel.mainAccounts.collectAsState()
    val searchedAccounts by sharedViewModel.searchedAccounts.collectAsState()

    DisplaySnackBar(snackBarHostState = snackBarHostState, onComplete = { sharedViewModel.action.value = it }, action = action,
        accountTitle = sharedViewModel.title.value) {
        sharedViewModel.action.value = it
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = { HomeTopAppBar(topAppBarState, sharedViewModel, scrollBehavior) },
        content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .padding(horizontal = 16.dp)
                    .fillMaxSize()
            ) {
                when{
                    topAppBarState == TopAppBarState.TRIGGERED -> {
                        if (searchedAccounts is RequestState.Success)
                            HomeSearchContent(
                                searchedAccounts = (searchedAccounts as RequestState.Success<List<Account>>).data,
                                navigateToItemScreen = navigateToItemScreen, sharedViewModel = sharedViewModel
                            )
                    }
                    else -> {
                        if (topAccounts is RequestState.Success && mainAccount is RequestState.Success) {
                            HomeContent(
                                allAccounts = (mainAccount as RequestState.Success<List<Account>>).data,
                                allAccountSortStrategy = AccountSortStrategy.LastCreated,
                                topAccounts = (topAccounts as RequestState.Success<List<Account>>).data, sharedViewModel, navigateToItemScreen = navigateToItemScreen
                            )
                        } else if (topAccounts !is RequestState.Error && mainAccount !is RequestState.Error) {
                            println(topAccounts.toString() + "" + mainAccount.toString())
                            AccountContentPlaceholder("等待数据中")//todo shining
                        } else {
                            AccountContentPlaceholder("读取账户失败，" + ((topAccounts as RequestState.Error).error.localizedMessage ?: "未知错误"))
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
        TopAccountsContent(allAccounts = topAccounts)
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
