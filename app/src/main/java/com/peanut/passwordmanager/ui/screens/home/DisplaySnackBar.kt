package com.peanut.passwordmanager.ui.screens.home

import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarResult
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.peanut.passwordmanager.R
import com.peanut.passwordmanager.util.Action
import kotlinx.coroutines.launch

@Composable
fun DisplaySnackBar(scaffoldState: ScaffoldState,
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
                val snackBarResult = scaffoldState.snackbarHostState.showSnackbar(
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