package com.peanut.passwordmanager.ui.screens.home

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import com.peanut.passwordmanager.data.models.Account
import com.peanut.passwordmanager.ui.viewmodel.SharedViewModel
import com.peanut.passwordmanager.util.Action
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AllAccountsContent(allAccounts: List<Account>, navigateToItemScreen: (Int) -> Unit, sharedViewModel: SharedViewModel){
    LazyColumn {
        items(items = allAccounts, key = { account: Account ->
            account.id
        }) { account ->
            val dismissState = rememberDismissState()
            val dismissDirection = dismissState.dismissDirection
            val isDismissed = dismissState.isDismissed(DismissDirection.EndToStart)
            if (isDismissed && dismissDirection == DismissDirection.EndToStart){
                val scope = rememberCoroutineScope()
                scope.launch {
                    delay(300)
                    sharedViewModel.action.value = Action.DELETE
                    //更新被删除的值，方便再添加
                    sharedViewModel.updateAccountFields(account)
                }
            }
            val degrees by animateFloatAsState(targetValue = if (dismissState.targetValue == DismissValue.Default) 0f else -45f )
            var itemAppeared by remember { mutableStateOf(false)}
            LaunchedEffect(key1 = true, block = { itemAppeared = true })
            AnimatedVisibility(visible = itemAppeared && !isDismissed, enter = expandVertically(animationSpec = tween(durationMillis = 300)),
                exit = shrinkVertically(animationSpec = tween(durationMillis = 300))) {
                SwipeToDismiss(state = dismissState, directions = setOf(DismissDirection.EndToStart), dismissThresholds = { FractionalThreshold(0.2f) },
                    background = { RedBackground(degrees = degrees)}) {
                    SmallAccountItem(account = account, navigateToItemScreen = navigateToItemScreen, sharedViewModel = sharedViewModel)
                }
            }
        }
    }
}