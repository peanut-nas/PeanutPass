package com.peanut.passwordmanager.ui.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peanut.passwordmanager.R
import com.peanut.passwordmanager.util.guessTypeFromString

@Composable
fun PasswordText(password: List<Pair<String, Int>>, paddingValues: PaddingValues = PaddingValues(12.dp)) {
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
    }, modifier = Modifier.padding(paddingValues), fontFamily = FontFamily(Font(R.font.ubuntu_mono)), fontSize = 20.sp, textAlign = TextAlign.Center)
}


@Composable
fun EditablePasswordText(password: List<Pair<String, Int>>, paddingValues: PaddingValues = PaddingValues(0.dp), onPasswordChange: (List<Pair<String, Int>>) -> Unit) {
    val primary = MaterialTheme.colorScheme.primary
    val secondary = MaterialTheme.colorScheme.secondary
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequester.freeFocus() // 确保初始不获得焦点
    }

    TextField(password.joinToString("") { it.first },
        onValueChange = { onPasswordChange(guessTypeFromString(it)) },
        modifier = Modifier
            .padding(paddingValues)
            .focusRequester(focusRequester),
        keyboardActions = KeyboardActions(onDone = {
            keyboardController?.hide()
            focusManager.clearFocus()
        }),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Ascii,
        ),
        textStyle = TextStyle(
            fontFamily = FontFamily(Font(R.font.ubuntu_mono)),
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        ),
        colors = androidx.compose.material3.TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            cursorColor = MaterialTheme.colorScheme.onSurface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        visualTransformation = { _ ->
            TransformedText(buildAnnotatedString {
                for (i in password) {
                    when (i.second) {
                        1 -> withStyle(style = SpanStyle(color = primary)) {
                            append(i.first)
                        }

                        2 -> withStyle(style = SpanStyle(color = secondary)) {
                            append(i.first)
                        }

                        else -> append(i.first)
                    }
                    append("\u200a")
                }
            }, PasswordOffsetMapping)
        }
    )
}

private object PasswordOffsetMapping : OffsetMapping {
    override fun originalToTransformed(offset: Int): Int {
        // 每个字符在显示时占据 2 个位置（字符+空格）
        return offset * 2
    }

    override fun transformedToOriginal(offset: Int): Int {
        // 将显示位置映射回原始位置
        return (offset + 1) / 2
    }
}

@Composable
@Preview(showBackground = true)
fun PasswordTextPreview() {
    PasswordText(password = guessTypeFromString("点我生成密码"))
}