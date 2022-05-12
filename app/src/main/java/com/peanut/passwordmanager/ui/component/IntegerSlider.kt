package com.peanut.passwordmanager.ui.component

import androidx.compose.material3.Slider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun IntegerSlider(initialValue:Int,
                  modifier: Modifier = Modifier,
                  valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
                  onValueChange:((Int)->Unit)?=null){
    var value by remember { mutableStateOf(initialValue.toFloat()) }
    LaunchedEffect(key1 = value){
        onValueChange?.invoke(value.toInt())
    }
    Slider(
        value = value,
        onValueChange = { value = it },
        modifier = modifier,
        valueRange = valueRange
    )
}