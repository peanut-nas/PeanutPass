package com.peanut.passwordmanager.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import com.peanut.passwordmanager.ui.component.SmallAccountItem
import com.peanut.passwordmanager.ui.component.AddFloatActionButton
import com.peanut.passwordmanager.ui.component.HomeTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navigateToItemScreen: (Int) -> Unit) {
    val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }
    Scaffold(
        topBar = { HomeTopAppBar(scrollBehavior = scrollBehavior) },
        content = {
            LazyColumn(modifier = Modifier.padding(it)) {
                items((1..19).toList()) {
                    SmallAccountItem()
                }
            }
        },
        floatingActionButton = {
            AddFloatActionButton {
                println("AddFloatActionButton")
                navigateToItemScreen(-1)
            }
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    )
}

@Composable
@Preview(showBackground = true)
fun HomeScreenPreview() {
    HomeScreen(navigateToItemScreen = {})
}