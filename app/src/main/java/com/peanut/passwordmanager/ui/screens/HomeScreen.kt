package com.peanut.passwordmanager.ui.screens

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.peanut.passwordmanager.ui.component.AddFloatActionButton
import com.peanut.passwordmanager.ui.component.HomeTopAppBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navigateToItemScreen: (Int) -> Unit){
    Scaffold(
        topBar = { HomeTopAppBar() },
        content = {},
        floatingActionButton = {
            AddFloatActionButton{
                println("AddFloatActionButton")
                navigateToItemScreen(-1)
            }
        }
    )
}

@Composable
@Preview(showBackground = true)
fun HomeScreenPreview(){
    HomeScreen(navigateToItemScreen = {})
}