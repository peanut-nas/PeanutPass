package com.peanut.passwordmanager.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.insets.statusBarsHeight
import com.peanut.passwordmanager.ui.theme.SheetScrimColor
import com.peanut.passwordmanager.ui.theme.SheetShape
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetPasswordGenerator(onPasswordChanged: (List<Pair<String, Int>>) -> Unit) {
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val containerColor = MaterialTheme.colorScheme.surfaceVariant
    val contentColor = contentColorFor(containerColor)
    val coroutineScope = rememberCoroutineScope()
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
                    fontSize = 25.sp,
                    fontFamily = FontFamily.SansSerif
                )
                PasswordGenerator(onPasswordChanged = { password ->
                    onPasswordChanged.invoke(
                        password
                    )
                }) {
                    coroutineScope.launch { sheetState.hide() }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        },
        scrimColor = SheetScrimColor
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(
                modifier = Modifier
                    .statusBarsHeight()
                    .fillMaxWidth()
            )
            Text(
                text = "Open Bottom Sheet",
                modifier = Modifier.clickable { coroutineScope.launch { sheetState.show() } })
        }
    }
}