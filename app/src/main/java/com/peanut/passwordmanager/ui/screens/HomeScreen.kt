package com.peanut.passwordmanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.peanut.passwordmanager.data.models.Account
import com.peanut.passwordmanager.ui.component.AddFloatActionButton
import com.peanut.passwordmanager.ui.component.HomeTopAppBar
import com.peanut.passwordmanager.ui.component.LargeAccountItem
import com.peanut.passwordmanager.ui.component.SmallAccountItem
import com.peanut.passwordmanager.ui.viewmodel.SharedViewModel
import com.peanut.passwordmanager.R
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
                        AllAccountsContent(allAccounts = (allAccounts as RequestState.Success<List<Account>>).data, navigateToItemScreen = navigateToItemScreen)
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

@Composable
fun AccountContentPlaceholder(text: String){
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(modifier = Modifier.size(120.dp), painter = painterResource(id = R.drawable.ic_sad_face), contentDescription = "Sad Face Icon", tint = MaterialTheme.colorScheme.onSurface.copy(ContentAlpha.medium))
        Text(text = text, color = MaterialTheme.colorScheme.onSurface.copy(ContentAlpha.medium), fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.bodyMedium.fontSize)
    }
}

@Composable
@Preview(showBackground = true)
fun EmptyAccountContentPreview(){
    AccountContentPlaceholder(stringResource(id = R.string.empty_content))
}

@Composable
fun AllAccountsContent(allAccounts: List<Account>, navigateToItemScreen: (Int) -> Unit){
    LazyColumn {
        items(items = allAccounts, key = { account: Account ->
            account.id
        }) { account ->
            SmallAccountItem(account = account, navigateToItemScreen = navigateToItemScreen)
        }
    }
}

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