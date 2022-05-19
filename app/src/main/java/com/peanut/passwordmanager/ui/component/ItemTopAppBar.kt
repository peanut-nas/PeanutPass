package com.peanut.passwordmanager.ui.component

import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.peanut.passwordmanager.data.models.Account
import com.peanut.passwordmanager.util.Action

@Composable
fun ItemTopAppBar(scrollBehavior: TopAppBarScrollBehavior? = null, navigateToHomeScreen: (Action) -> Unit, selectedAccount: Account?) {
    if (selectedAccount == null){
        AddTopAppBar(scrollBehavior = scrollBehavior, navigateToHomeScreen = navigateToHomeScreen)
    }else{
        ExistingTopAppBar(selectedAccount = selectedAccount, navigateToHomeScreen = navigateToHomeScreen)
    }
}

@Composable
fun AddTopAppBar(scrollBehavior: TopAppBarScrollBehavior? = null, navigateToHomeScreen: (Action) -> Unit){
    BaseTopAppBar(
        title = {
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Add Account")
                    }
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        navigationIcon = {
                         GoBackAction(onBackClicked = navigateToHomeScreen)
        },
        actions = {
            AddAction(onAddClicked = navigateToHomeScreen)
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
fun ExistingTopAppBar(selectedAccount: Account, scrollBehavior: TopAppBarScrollBehavior? = null, navigateToHomeScreen: (Action) -> Unit){
    BaseTopAppBar(
        title = {
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(selectedAccount.title)
                    }
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        navigationIcon = {
            CloseAction(onCloseClicked = navigateToHomeScreen)
        },
        actions = {
            DeleteAction(onDeleteClicked = navigateToHomeScreen)
            UpdateAction(onUpdateClicked = navigateToHomeScreen)
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
@Preview
fun AddTopAppBarPreview(){
    AddTopAppBar(navigateToHomeScreen = {})
}

@Composable
@Preview
fun ETopAppBarPreview(){
    ExistingTopAppBar(selectedAccount = Account(0, "QQ", "", "290120506", ""), navigateToHomeScreen = {})
}

