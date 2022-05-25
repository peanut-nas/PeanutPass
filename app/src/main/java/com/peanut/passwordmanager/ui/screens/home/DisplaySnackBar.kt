package com.peanut.passwordmanager.ui.screens.home

import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import com.peanut.passwordmanager.R
import com.peanut.passwordmanager.util.Action
import kotlinx.coroutines.launch

@Composable
fun DisplaySnackBar(snackbarHostState: SnackbarHostState,
                    onComplete: (Action) -> Unit,
                    action: Action,
                    accountTitle: String,
                    onUndoClicked: (Action) -> Unit){
    val scope = rememberCoroutineScope()
    val undo = stringResource(id = R.string.action_undo)
    val ok = stringResource(id = R.string.action_ok)
    val actionName = stringResource(id = action.actionNameStringResourceId)
    LaunchedEffect(key1 = action){
        if (action != Action.NO_ACTION){
            scope.launch {
                val snackBarResult = snackbarHostState.showSnackbar(
                    message = "$actionName: $accountTitle",
                    actionLabel = setActionLabel(action, undo, ok)
                )
                undoDeleteAccount(action, snackBarResult, onUndoClicked)
            }
            onComplete(Action.NO_ACTION)
        }
    }
}

private fun setActionLabel(action: Action, undo: String, ok: String): String{
    return if (action.name == "DELETE")
        undo
    else ok
}

private fun undoDeleteAccount(action: Action, snackBarResult: SnackbarResult, onUndoClicked: (Action) -> Unit){
    if (snackBarResult == SnackbarResult.ActionPerformed && action == Action.DELETE)
        onUndoClicked(Action.UNDO)
}