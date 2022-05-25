package com.peanut.passwordmanager.ui.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.peanut.passwordmanager.R
import com.peanut.passwordmanager.data.models.Account
import com.peanut.passwordmanager.ui.viewmodel.SharedViewModel
import com.peanut.passwordmanager.util.AccountSortStrategy
import com.peanut.passwordmanager.util.Action
import com.peanut.passwordmanager.util.RequestState
import com.peanut.passwordmanager.util.TopAppBarState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(action: Action, navigateToItemScreen: (Int) -> Unit, sharedViewModel: SharedViewModel) {
    val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }
    val topAppBarState by sharedViewModel.topAppBarState
    val snackbarHostState = remember { SnackbarHostState() }
    DisplaySnackBar(snackbarHostState = snackbarHostState, onComplete = { sharedViewModel.action.value = it }, action = action, accountTitle = sharedViewModel.title.value) {
        sharedViewModel.action.value = it
    }
    LaunchedEffect(key1 = true) {
        sharedViewModel.getAllAccounts()
    }
    LaunchedEffect(key1 = action) {
        sharedViewModel.handleDatabaseActions(action = action)
    }
    val allAccounts by sharedViewModel.allAccounts.collectAsState()
    val searchedAccounts by sharedViewModel.searchedAccounts.collectAsState()
    //todo: replace with material3 when passable
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
                if (topAppBarState == TopAppBarState.TRIGGERED) {
                    if (searchedAccounts is RequestState.Success)
                        HomeSearchContent(
                            searchedAccounts = (searchedAccounts as RequestState.Success<List<Account>>).data,
                            navigateToItemScreen = navigateToItemScreen, sharedViewModel = sharedViewModel
                        )
                } else {
                    if (allAccounts is RequestState.Success) {
                        HomeContent(
                            allAccounts = (allAccounts as RequestState.Success<List<Account>>).data,
                            topAccounts = (allAccounts as RequestState.Success<List<Account>>).data, sharedViewModel, navigateToItemScreen = navigateToItemScreen
                        )
                    } else if (allAccounts is RequestState.Error) {
                        AccountContentPlaceholder((allAccounts as RequestState.Error).error.localizedMessage ?: "出现未知错误")
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
fun HomeContent(allAccounts: List<Account>, topAccounts: List<Account>, sharedViewModel: SharedViewModel, navigateToItemScreen: (Int) -> Unit) {
    if (allAccounts.isEmpty())
        AccountContentPlaceholder(stringResource(id = R.string.empty_content))
    else {
        SelectableTitle(title = stringResource(id = AccountSortStrategy.LastRecentUsed.sortStrategyStringResourceId),
            isSelectable = false, items = emptyArray(), onSelected = {})
        TopAccountsContent(allAccounts = topAccounts, navigateToItemScreen = navigateToItemScreen)
        SelectableTitle(
            title = stringResource(id = AccountSortStrategy.LastFrequentUsed.sortStrategyStringResourceId), isSelectable = true,
            items = arrayOf(
                stringResource(id = AccountSortStrategy.LastRecentUsed.sortStrategyStringResourceId),
                stringResource(id = AccountSortStrategy.LastFrequentUsed.sortStrategyStringResourceId),
                stringResource(id = AccountSortStrategy.LastCreated.sortStrategyStringResourceId),
            ), onSelected = {})
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
