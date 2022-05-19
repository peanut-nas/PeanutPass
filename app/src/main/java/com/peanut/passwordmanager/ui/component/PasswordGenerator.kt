package com.peanut.passwordmanager.ui.component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.peanut.passwordmanager.util.SettingManager
import com.peanut.passwordmanager.util.generatePassword

@Composable
fun PasswordGenerator(
    onPasswordChanged: (List<Pair<String, Int>>) -> Unit,
    onDone: (() -> Unit)? = null
) {
    var length by remember { mutableStateOf(SettingManager.getValue("PasswordGeneratorLength", 12)) }
    var useLetter by remember { mutableStateOf(SettingManager.getValue("PasswordGeneratorUseLetter", true)) }
    var useUpperLetter by remember { mutableStateOf(SettingManager.getValue("PasswordGeneratorUseUpperLetter", true)) }
    var useNumber by remember { mutableStateOf(SettingManager.getValue("PasswordGeneratorUseNumber", true)) }
    var useSymbol by remember { mutableStateOf(SettingManager.getValue("PasswordGeneratorUseSymbol", true)) }
    var password by remember {
        mutableStateOf(
            generatePassword(length = length, useLetter = useLetter, useUpperLetter = useUpperLetter, useNumber = useNumber, useSymbol = useSymbol)
        )
    }
    LaunchedEffect(length, useLetter, useUpperLetter, useNumber, useSymbol) {
        password = generatePassword(
            length = length, useLetter = useLetter, useUpperLetter = useUpperLetter, useNumber = useNumber, useSymbol = useSymbol
        )
        SettingManager["PasswordGeneratorLength"] = length
        SettingManager["PasswordGeneratorUseLetter"] = useLetter
        SettingManager["PasswordGeneratorUseUpperLetter"] = useUpperLetter
        SettingManager["PasswordGeneratorUseNumber"] = useNumber
        SettingManager["PasswordGeneratorUseSymbol"] = useSymbol
    }
    LaunchedEffect(password) {
        onPasswordChanged.invoke(password)
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .animateContentSize(animationSpec = tween(durationMillis = 500)),
            shape = MaterialTheme.shapes.medium.copy(CornerSize(10.dp)),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f)
        ) {
            PasswordText(password)
        }
        Row(modifier = Modifier.padding(top = 8.dp)) {
            TextCheckbox(
                checked = useLetter,
                onCheckedChange = { useLetter = !useLetter },
                colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.onSurface),
                text = "字母"
            )
            TextCheckbox(
                checked = useUpperLetter,
                onCheckedChange = { useUpperLetter = !useUpperLetter }, text = "大写字母",
                colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.onSurface),
            )
            TextCheckbox(
                checked = useNumber,
                onCheckedChange = { useNumber = !useNumber },
                colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary),
                text = "数字"
            )
            TextCheckbox(
                checked = useSymbol,
                onCheckedChange = { useSymbol = !useSymbol },
                colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.secondary),
                text = "符号"
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "长度")
            IntegerSlider(
                initialValue = length,
                onValueChange = { if (length != it) length = it },
                modifier = Modifier.fillMaxWidth(0.85f),
                valueRange = 8f..24f
            )
            Text(text = "${length}位")
        }
        Row(
            modifier = Modifier.fillMaxWidth(0.9f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = {
                password =
                    generatePassword(
                        length = length, useLetter = useLetter, useUpperLetter = useUpperLetter, useNumber = useNumber, useSymbol = useSymbol
                    )
            }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.Refresh, contentDescription = "重新生成一个密码")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "不满意？点我重新生成")
                }
            }
            Button(onClick = {
                onDone?.invoke()
                onPasswordChanged.invoke(password)
            }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.Done, contentDescription = "选择这个密码")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "选择")
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PasswordGeneratorPreview(){
    SettingManager.init(LocalContext.current)
    PasswordGenerator(onPasswordChanged = {})
}