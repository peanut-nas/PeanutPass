package com.peanut.passwordmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.statusBarsHeight
import com.peanut.passwordmanager.ui.theme.PasswordManagerTheme
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SettingManager.init(this)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            PasswordManagerTheme {
                ProvideWindowInsets {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        BottomSheet()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheet() {
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
                Spacer(modifier = Modifier.height(18.dp))
                Text(
                    text = "Strong, Secure & Stochastic",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 25.sp,
                    fontFamily = FontFamily.SansSerif
                )
                Spacer(modifier = Modifier.height(18.dp))
                PasswordGenerator {
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

private val SheetScrimColor = Black.copy(alpha = 0.4f)
private val SheetShape = RoundedCornerShape(
    topStart = 16.dp,
    topEnd = 16.dp,
    bottomEnd = 0.dp,
    bottomStart = 0.dp
)

@Composable
fun PasswordGenerator(onDone: (() -> Unit)? = null) {
    var length by remember {
        mutableStateOf(
            SettingManager.getValue(
                "PasswordGeneratorLength",
                12f
            )
        )
    }
    var useLetter by remember {
        mutableStateOf(
            SettingManager.getValue(
                "PasswordGeneratorUseLetter",
                true
            )
        )
    }
    var useUpperLetter by remember {
        mutableStateOf(
            SettingManager.getValue(
                "PasswordGeneratorUseUpperLetter",
                true
            )
        )
    }
    var useNumber by remember {
        mutableStateOf(
            SettingManager.getValue(
                "PasswordGeneratorUseNumber",
                true
            )
        )
    }
    var useSymbol by remember {
        mutableStateOf(
            SettingManager.getValue(
                "PasswordGeneratorUseSymbol",
                true
            )
        )
    }
    var password by remember {
        mutableStateOf(
            generatePassword(
                length = length,
                useLetter = useLetter,
                useUpperLetter = useUpperLetter,
                useNumber = useNumber,
                useSymbol = useSymbol
            )
        )
    }
    LaunchedEffect(length, useLetter, useUpperLetter, useNumber, useSymbol) {
        password = generatePassword(
            length = length,
            useLetter = useLetter,
            useUpperLetter = useUpperLetter,
            useNumber = useNumber,
            useSymbol = useSymbol
        )
        SettingManager["PasswordGeneratorLength"] = length
        SettingManager["PasswordGeneratorUseLetter"] = useLetter
        SettingManager["PasswordGeneratorUseUpperLetter"] = useUpperLetter
        SettingManager["PasswordGeneratorUseNumber"] = useNumber
        SettingManager["PasswordGeneratorUseSymbol"] = useSymbol
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
            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(buildAnnotatedString {
                    for (i in password) {
                        when (i.second) {
                            1 -> withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                                append(i.first)
                            }
                            2 -> withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.secondary)) {
                                append(i.first)
                            }
                            else -> append(i.first)
                        }
                        append("\u200a")
                    }
                }, fontFamily = FontFamily(Font(R.font.ubuntu_mono)), fontSize = 20.sp)
            }
        }
        Row {
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
            modifier = Modifier.fillMaxWidth(0.9f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "长度")
            Slider(
                value = length,
                onValueChange = {
                    if (length.toInt() != it.toInt())
                        length = it
                },
                modifier = Modifier.fillMaxWidth(0.85f),
                valueRange = 8f..24f
            )
            Text(text = "${length.toInt()}位")
        }
        Row(
            modifier = Modifier.fillMaxWidth(0.9f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = {
                password =
                    generatePassword(
                        length = length,
                        useLetter = useLetter,
                        useUpperLetter = useUpperLetter,
                        useNumber = useNumber,
                        useSymbol = useSymbol
                    )
            }) {
                Row {
                    Icon(Icons.Rounded.Refresh, contentDescription = "重新生成一个密码")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "不满意？点我重新生成")
                }
            }
            Button(onClick = {
                onDone?.invoke()
            }) {
                Row {
                    Icon(Icons.Rounded.Done, contentDescription = "选择这个密码")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "选择")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextCheckbox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: CheckboxColors = CheckboxDefaults.colors()
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked, onCheckedChange, modifier, enabled, interactionSource, colors)
        Text(text = text)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PasswordManagerTheme {
        PasswordGenerator()
    }
}

fun generatePassword(
    length: Float,
    useLetter: Boolean = false,
    useUpperLetter: Boolean = false,
    useNumber: Boolean = false,
    useSymbol: Boolean = false
): List<Pair<Char, Int>> {
    if (!(useLetter || useUpperLetter || useNumber || useSymbol))
        return emptyList()
    val basic = "abcdefghijklmnopqrstuvwxyz"
    val upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val number = "0123456789"
    val symbol = "!@#$%^&*()_+-={}[]|:;'\"\\,<.>/?`~"
    val candidates =
        (if (useLetter) basic else "") + (if (useUpperLetter) upper else "") + (if (useNumber) number else "") + (if (useSymbol) symbol else "")
    val sb = mutableListOf<Pair<Char, Int>>()
    for (i in 0 until length.toInt()) {
        val selected = candidates[(Math.random() * candidates.length).toInt()]
        val type = if (selected in basic || selected in upper) 0 else {
            if (selected in number) 1 else 2
        }
        sb.add(selected to type)
    }
    return sb
}