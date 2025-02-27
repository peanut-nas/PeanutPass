package com.peanut.passwordmanager.ui.screens.item

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import com.peanut.passwordmanager.R
import com.peanut.passwordmanager.ui.component.AddAction
import com.peanut.passwordmanager.ui.component.BaseTopAppBar
import com.peanut.passwordmanager.ui.component.GoBackAction
import com.peanut.passwordmanager.util.Action

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTopAppBar(scrollBehavior: TopAppBarScrollBehavior? = null, navigateToHomeScreen: (Action) -> Unit){
    BaseTopAppBar(
        title = {
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(stringResource(id = R.string.add_account))
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