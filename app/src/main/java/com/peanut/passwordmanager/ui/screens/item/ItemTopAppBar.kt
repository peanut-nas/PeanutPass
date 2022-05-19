package com.peanut.passwordmanager.ui.screens.item

import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
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
@Preview
fun AddTopAppBarPreview(){
    AddTopAppBar(navigateToHomeScreen = {})
}

@Composable
@Preview
fun ETopAppBarPreview(){
    ExistingTopAppBar(selectedAccount = Account(0, "QQ", "", "290120506", ""), navigateToHomeScreen = {})
}

