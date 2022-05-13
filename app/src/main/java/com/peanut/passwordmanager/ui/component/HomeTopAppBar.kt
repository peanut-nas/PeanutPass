package com.peanut.passwordmanager.ui.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.insets.ui.TopAppBar
import com.peanut.passwordmanager.R

@Composable
fun HomeTopAppBar() {
    DefaultHomeTopAppBar(onSearch = {})
}

@Composable
fun DefaultHomeTopAppBar(onSearch: () -> Unit) {
    TopAppBar(title = {
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.titleMedium,
            fontFamily = FontFamily.SansSerif
        )
    }, backgroundColor = MaterialTheme.colorScheme.background,
        actions = {
            SearchAction { onSearch() }
        })
}

@Composable
@Preview
fun DefaultHomeTopAppBarPreview() {
    DefaultHomeTopAppBar(onSearch = {})
}