package com.peanut.passwordmanager.ui.screens.item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peanut.passwordmanager.ui.component.PasswordGenerator
import com.peanut.passwordmanager.util.clearType
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetPasswordGenerator(sheetState: SheetState, onDismissRequest: () -> Unit, onPasswordChanged: (String) -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Strong, Secure & Stochastic",
                modifier = Modifier.padding(bottom = 10.dp),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 23.sp,
                fontFamily = FontFamily.SansSerif
            )
            PasswordGenerator(onPasswordChanged = { password ->
                if (sheetState.currentValue == SheetValue.Expanded) onPasswordChanged(password.clearType())
            }) {
                coroutineScope.launch { sheetState.hide() }.invokeOnCompletion { onDismissRequest() }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}