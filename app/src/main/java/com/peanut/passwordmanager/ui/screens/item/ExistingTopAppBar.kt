package com.peanut.passwordmanager.ui.screens.item

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import com.peanut.passwordmanager.data.models.Account
import com.peanut.passwordmanager.ui.component.BaseTopAppBar
import com.peanut.passwordmanager.ui.component.CloseAction
import com.peanut.passwordmanager.ui.component.DeleteAction
import com.peanut.passwordmanager.ui.component.UpdateAction
import com.peanut.passwordmanager.util.Action

@OptIn(ExperimentalMaterial3Api::class)
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