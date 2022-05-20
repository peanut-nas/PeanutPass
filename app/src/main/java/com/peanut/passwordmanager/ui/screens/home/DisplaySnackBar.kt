package com.peanut.passwordmanager.ui.screens.home

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.peanut.passwordmanager.util.Action
import kotlinx.coroutines.launch

@Composable
fun DisplaySnackBar(scaffoldState: ScaffoldState, handleDatabaseActions: () -> Unit, action: Action, accountTitle: String){
    handleDatabaseActions()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = action){
        if (action != Action.NO_ACTION){
            scope.launch {
                val snackBarResult = scaffoldState.snackbarHostState.showSnackbar(
                    message = "${context.resources.getString(action.actionNameStringResourceId)}: $accountTitle",
                    actionLabel = "OK"
                )
            }
        }
    }
}