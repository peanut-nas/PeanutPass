package com.peanut.passwordmanager.ui.screens.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.peanut.passwordmanager.util.AccountType

@Composable
fun SelectableTitle(title: String, isSelectable: Boolean, items: Array<String>, onSelected: (String) -> Unit){
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
                    DropdownMenuItem(text = { Text(text = item) }, onClick = {
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
        SelectableTitle(title = "Last used", isSelectable = false, items = emptyArray(), onSelected = {})
        SelectableTitle(title = "Most used", isSelectable = true, items = arrayOf("Most used", "Last used", "Last create"), onSelected = {})
    }

}