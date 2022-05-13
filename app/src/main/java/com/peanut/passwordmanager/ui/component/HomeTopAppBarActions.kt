package com.peanut.passwordmanager.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.peanut.passwordmanager.R

@Composable
fun SearchAction(onSearchClicked: () -> Unit) {
    IconButton(onClick = { onSearchClicked() }) {
        Icon(imageVector = Icons.Rounded.Search, contentDescription = stringResource(id = R.string.search_your_accounts))
    }
}