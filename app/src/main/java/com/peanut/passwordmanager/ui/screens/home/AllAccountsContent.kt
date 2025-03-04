package com.peanut.passwordmanager.ui.screens.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import com.peanut.passwordmanager.data.models.Account
import com.peanut.passwordmanager.ui.viewmodel.SharedViewModel
import com.peanut.passwordmanager.util.Action
import kotlinx.coroutines.launch

@Composable
fun AllAccountsContent(allAccounts: List<Account>, navigateToItemScreen: (Int) -> Unit, sharedViewModel: SharedViewModel) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(items = allAccounts, key = { account: Account ->
            account.id
        }) { account ->
            val scope = rememberCoroutineScope()
            val dismissState = rememberSwipeToDismissBoxState(
                positionalThreshold = { it * 0.2f },
                confirmValueChange = { newValue ->
                    if(newValue == SwipeToDismissBoxValue.EndToStart){
                        scope.launch {
                            sharedViewModel.action.value = Action.DELETE
                            //更新被删除的值，方便再添加
                            sharedViewModel.updateAccountFields(account)
                        }
                        true
                    } else {
                        false
                    }
                }
            )
            val degrees by animateFloatAsState(targetValue = if (dismissState.progress != 1f && dismissState.progress > 0.2f ) -45f else 0f)
            SwipeToDismissBox(
                state = dismissState,
                backgroundContent = {
                    RedBackground(degrees = degrees)
                },
                enableDismissFromStartToEnd = false,
                content = { SmallAccountItem(account = account, navigateToItemScreen = navigateToItemScreen, sharedViewModel = sharedViewModel) }
            )
        }
    }
}