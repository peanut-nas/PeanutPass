package com.peanut.passwordmanager.ui.screens.item

import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peanut.passwordmanager.ui.component.PasswordGenerator
import com.peanut.passwordmanager.ui.theme.SheetScrimColor
import com.peanut.passwordmanager.ui.theme.SheetShape
import com.peanut.passwordmanager.util.clearType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetPasswordGenerator(sheetState: ModalBottomSheetState, coroutineScope: CoroutineScope, content: @Composable () -> Unit, onPasswordChanged: (String) -> Unit) {
    val containerColor = MaterialTheme.colorScheme.surfaceVariant
    val contentColor = contentColorFor(containerColor)
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetBackgroundColor = containerColor,
        sheetContentColor = contentColor,
        sheetShape = SheetShape,
        sheetContent = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Strong, Secure & Stochastic",
                    modifier = Modifier.padding(0.dp, 20.dp),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 23.sp,
                    fontFamily = FontFamily.SansSerif
                )
                PasswordGenerator(onPasswordChanged = { password ->
                    onPasswordChanged(password.clearType())
                }) {
                    coroutineScope.launch { sheetState.hide() }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        },
        scrimColor = SheetScrimColor,
        content = content
    )
}