package com.peanut.passwordmanager.ui.component

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.peanut.passwordmanager.R
import com.peanut.passwordmanager.util.Action
import com.peanut.passwordmanager.util.AdditionalFunctions.copy

@Composable
fun SearchAction(onSearchClicked: () -> Unit) {
    IconButton(onClick = { onSearchClicked() }) {
        Icon(imageVector = Icons.Rounded.Search, contentDescription = stringResource(id = R.string.search_your_accounts))
    }
}

@Composable
fun GoBackAction(onBackClicked: (Action) -> Unit){
    IconButton(onClick = { onBackClicked(Action.NO_ACTION) }) {
        Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = stringResource(id = R.string.go_back))
    }
}

@Composable
fun CloseAction(onCloseClicked: (Action) -> Unit){
    IconButton(onClick = { onCloseClicked(Action.NO_ACTION) }) {
        Icon(imageVector = Icons.Rounded.Close, contentDescription = stringResource(id = R.string.close_account))
    }
}

@Composable
fun AddAction(onAddClicked: (Action) -> Unit){
    IconButton(onClick = { onAddClicked(Action.ADD) }) {
        Icon(imageVector = Icons.Rounded.Check, contentDescription = stringResource(id = R.string.add_account))
    }
}

@Composable
fun DeleteAction(onDeleteClicked: (Action) -> Unit){
    IconButton(onClick = { onDeleteClicked(Action.DELETE) }) {
        Icon(imageVector = Icons.Rounded.Delete, contentDescription = stringResource(id = R.string.delete_account))
    }
}

@Composable
fun UpdateAction(onUpdateClicked: (Action) -> Unit){
    IconButton(onClick = { onUpdateClicked(Action.UPDATE) }) {
        Icon(imageVector = Icons.Rounded.Check, contentDescription = stringResource(id = R.string.delete_account))
    }
}

@Composable
fun CopyAction(text: String){
    val context = LocalContext.current
    val message = stringResource(id = R.string.account_password_copy)
    IconButton(onClick = {
        text.copy(context)
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }) {
        Icon(painter = painterResource(id = R.drawable.ic_round_content_copy_24), contentDescription = null)
    }
}

@Composable
@Preview(showBackground = true)
fun ActionsPreview(){
    Column {
        SearchAction(onSearchClicked = {})
        GoBackAction(onBackClicked = {})
        CloseAction(onCloseClicked = {})
        AddAction(onAddClicked = {})
        DeleteAction(onDeleteClicked = {})
        UpdateAction(onUpdateClicked = {})
        CopyAction(text = "")
    }
}