package com.peanut.passwordmanager.ui.screens.home

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Surface
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import com.peanut.passwordmanager.data.models.Account
import com.peanut.passwordmanager.ui.viewmodel.SharedViewModel
import com.peanut.passwordmanager.util.Action
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AllAccountsContent(allAccounts: List<Account>, navigateToItemScreen: (Int) -> Unit, sharedViewModel: SharedViewModel) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(items = allAccounts, key = { account: Account ->
            account.id
        }) { account ->
            val dismissState = rememberDismissState()
            val dismissDirection = dismissState.dismissDirection
            val isDismissed = dismissState.isDismissed(DismissDirection.EndToStart)
            if (isDismissed && dismissDirection == DismissDirection.EndToStart) {
                val scope = rememberCoroutineScope()
                scope.launch {
                    delay(300)
                    sharedViewModel.action.value = Action.DELETE
                    //更新被删除的值，方便再添加
                    sharedViewModel.updateAccountFields(account)
                }
            }
            val degrees by animateFloatAsState(targetValue = if (dismissState.targetValue == DismissValue.Default) 0f else -45f)
            Surface(shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp))) {
                SwipeToDismiss(state = dismissState, directions = setOf(DismissDirection.EndToStart), dismissThresholds = { FractionalThreshold(0.2f) },
                    background = { RedBackground(degrees = degrees) }) {
                    SmallAccountItem(account = account, navigateToItemScreen = navigateToItemScreen, sharedViewModel = sharedViewModel)
                }
            }
        }
    }
}