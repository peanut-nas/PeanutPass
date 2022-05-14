package com.peanut.passwordmanager.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peanut.passwordmanager.data.models.Account
import com.peanut.passwordmanager.ui.theme.AccountIconBackground
import com.peanut.passwordmanager.util.AccountType

@Composable
fun SmallAccountItem(account: Account, navigateToItemScreen: (accountId: Int) -> Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(70.dp)
        .clickable { navigateToItemScreen(account.id) }) {
        AccountIcon(accountName = account.title)
        Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxHeight()) {
            Text(
                text = account.title,
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = getDisplayAccount(account),
                modifier = Modifier.padding(bottom = 12.dp),
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface.copy(ContentAlpha.disabled)),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

fun getDisplayAccount(account: Account): String{
    when(account.accountType){
        AccountType.Email -> {
            val p = account.account.split("@")
            return if (p[0].length > 4)
                "${p[0].substring(0..1)}${"*".repeat(p[0].length-4)}${p[0].substring(p[0].length-2)}@${p[1]}"
            else
                "${"*".repeat(p[0].length)}${p[1]}"
        }
        AccountType.CardNumber -> {
            return if (account.account.length > 4)
                "${account.account.substring(0..1)}${"*".repeat(account.account.length-4)}${account.account.substring(account.account.length-2)}"
            else
                "*".repeat(account.account.length)
        }
        AccountType.PhoneNumber -> {
            return "${account.account.substring(0..2)} **** ${account.account.substring(account.account.length-4)}"

        }
        AccountType.NickName -> {
            return account.account
        }
    }
}

@Composable
fun LargeAccountItem(account: Account, navigateToItemScreen: (accountId: Int) -> Unit) {
    Box(modifier = Modifier
        .height(94.dp)
        .clickable { navigateToItemScreen(account.id) }) {
        AccountIcon(accountName = account.title, width = 70.dp, fontSize = 36.sp)
    }
}

@Composable
private fun AccountIcon(accountName: String, icon: Painter? = null, width: Dp = 46.dp, fontSize: TextUnit = 24.sp) {
    Surface(modifier = Modifier
        .padding(12.dp)
        .width(width), shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp)), color = AccountIconBackground.copy(0.5f)) {
        if (icon != null)
            Icon(painter = icon, contentDescription = "Account Icon Background", tint = Color.Unspecified)
        else if (accountName.isNotEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = accountName[0].toString(), fontSize = fontSize)
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun AccountItemPreview() {
    Column {
        SmallAccountItem(Account(0, "Microsoft", "", "panrunqiu@outlook.com", "abc********def", AccountType.Email)){}
        SmallAccountItem(Account(0, "中国农业银行", "", "622848123456712345671234567", "abc********def", AccountType.CardNumber)){}
        SmallAccountItem(Account(0, "Microsoft", "", "17712341234", "abc********def", AccountType.PhoneNumber)){}
        SmallAccountItem(Account(0, "Bilibili", "", "花生酱啊啊啊啊", "abc********def", AccountType.NickName)){}
    }
}

@Composable
@Preview(showBackground = true)
fun LargeAccountItemPreview() {
    LargeAccountItem(Account(0, "Microsoft", "", "panrunqiu@outlook.com", "abc********def", AccountType.Email)){}
}