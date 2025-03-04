package com.peanut.passwordmanager.ui.screens.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun<T> SelectableTitle(title: String, isSelectable: Boolean, items: Array<T>, performText: (T) -> String, onSelected: (T) -> Unit){
    var expanded by remember { mutableStateOf(false) }
    val angle: Float by animateFloatAsState(targetValue = if (expanded) 180f else 0f)

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        if (isSelectable){
            IconButton(onClick = { expanded = true }) {
                Icon(modifier = Modifier.rotate(angle), imageVector = Icons.Rounded.ArrowDropDown, contentDescription = null)
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                for (item in items) {
                    DropdownMenuItem(text = { Text(text = performText(item)) }, onClick = {
                        expanded = false
                        onSelected(item)
                    })
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SelectableTitlePreview(){
    Column {
        SelectableTitle(title = "Last used", isSelectable = false, items = emptyArray<String>(), onSelected = {}, performText = {it})
        SelectableTitle(title = "Most used", isSelectable = true, items = arrayOf("Most used", "Last used", "Last create"), onSelected = {}, performText = {it})
    }

}