package com.peanut.passwordmanager.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.peanut.passwordmanager.ui.component.BottomSheetPasswordGenerator
import com.peanut.passwordmanager.util.Action

@Composable
fun ItemScreen(navigateToItemScreen: (Action) -> Unit){
    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        var password by remember { mutableStateOf(listOf("点我生成密码" to 1)) }
        BottomSheetPasswordGenerator { generated ->
            password = generated
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ItemScreen(){
}