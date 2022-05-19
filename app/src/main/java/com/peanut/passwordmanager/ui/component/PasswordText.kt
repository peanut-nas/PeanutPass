package com.peanut.passwordmanager.ui.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
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
@Preview(showBackground = true)
fun PasswordTextPreview(){
    PasswordText(password = guessTypeFromString("点我生成密码"))
}