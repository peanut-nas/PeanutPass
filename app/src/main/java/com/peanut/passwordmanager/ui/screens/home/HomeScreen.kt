package com.peanut.passwordmanager.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.rememberScaffoldState
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
import com.peanut.passwordmanager.util.TopAppBarState

@Composable
fun HomeScreen(navigateToItemScreen: (Int) -> Unit, sharedViewModel: SharedViewModel) {
    val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }
    val action by sharedViewModel.action
    val topAppBarState by sharedViewModel.topAppBarState

    //todo: bug: 实体键返回时没有清除Action
    val scaffoldState = rememberScaffoldState()
    DisplaySnackBar(scaffoldState = scaffoldState, handleDatabaseActions = { sharedViewModel.handleDatabaseActions(action = action) }, action = action, accountTitle = sharedViewModel.title.value){
        sharedViewModel.action.value = it
    }

    LaunchedEffect(key1 = true) {
        sharedViewModel.getAllAccounts()
    }
    val allAccounts by sharedViewModel.allAccounts.collectAsState()
    val searchedAccounts by sharedViewModel.searchedAccounts.collectAsState()
    //todo: replace with material3 when passable
    androidx.compose.material.Scaffold(
        scaffoldState = scaffoldState,
        topBar = { HomeTopAppBar(topAppBarState, sharedViewModel, scrollBehavior) },
        content = {
            Column(modifier = Modifier
                .padding(it)
                .fillMaxSize()) {
                if (topAppBarState == TopAppBarState.TRIGGERED) {
                    if (searchedAccounts is RequestState.Success)
                        HomeSearchContent(searchedAccounts = (searchedAccounts as RequestState.Success<List<Account>>).data,
                            navigateToItemScreen = navigateToItemScreen, sharedViewModel = sharedViewModel)
                }else{
                    if (allAccounts is RequestState.Success) {
                        HomeContent(allAccounts = (allAccounts as RequestState.Success<List<Account>>).data,
                            topAccounts = (allAccounts as RequestState.Success<List<Account>>).data, sharedViewModel, navigateToItemScreen = navigateToItemScreen)
                    }else if(allAccounts is RequestState.Error){
                        AccountContentPlaceholder((allAccounts as RequestState.Error).error.localizedMessage?:"出现未知错误")
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
fun HomeContent(allAccounts: List<Account>, topAccounts: List<Account>, sharedViewModel: SharedViewModel, navigateToItemScreen: (Int) -> Unit){
    if (allAccounts.isEmpty())
        AccountContentPlaceholder(stringResource(id = R.string.empty_content))
    else {
        TopAccountsContent(allAccounts = topAccounts, navigateToItemScreen = navigateToItemScreen)
        AllAccountsContent(allAccounts = allAccounts, navigateToItemScreen = navigateToItemScreen, sharedViewModel = sharedViewModel)
    }
}

@Composable
fun HomeSearchContent(searchedAccounts: List<Account>, sharedViewModel: SharedViewModel, navigateToItemScreen: (Int) -> Unit){
    if (searchedAccounts.isEmpty())
        AccountContentPlaceholder(stringResource(id = R.string.empty_content))
    else {
        AllAccountsContent(allAccounts = searchedAccounts, navigateToItemScreen = navigateToItemScreen, sharedViewModel = sharedViewModel)
    }
}
