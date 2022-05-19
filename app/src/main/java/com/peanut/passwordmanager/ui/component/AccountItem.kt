package com.peanut.passwordmanager.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import androidx.lifecycle.viewModelScope
import com.peanut.passwordmanager.data.models.Account
import com.peanut.passwordmanager.ui.theme.AccountIconBackground
import com.peanut.passwordmanager.ui.viewmodel.SharedViewModel
import com.peanut.passwordmanager.util.AccountType
import kotlinx.coroutines.launch

@Composable
fun SmallAccountItem(account: Account, navigateToItemScreen: (accountId: Int) -> Unit, sharedViewModel: SharedViewModel) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(70.dp)
        .clickable { navigateToItemScreen(account.id) }) {
        AccountIcon(accountName = account.title)
        Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxHeight()) {
            Text(
                text = account.title,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clickable { },
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, fontSize = 20.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            var actualAccountName by remember { mutableStateOf("") }
            LaunchedEffect(key1 = true) {
                sharedViewModel.viewModelScope.launch {
                    getDisplayAccount(account, sharedViewModel) { accountName ->
                        actualAccountName = accountName
                    }
                }
            }
            Text(
                text = actualAccountName,
                modifier = Modifier.padding(bottom = 12.dp),
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface.copy(ContentAlpha.disabled)),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        CopyAction(text = account.password)
    }
}

suspend fun getDisplayAccount(account: Account, sharedViewModel: SharedViewModel, prefix: String = "", onAccountNameFound: (String) -> Unit) {
    when (account.accountType) {
        AccountType.Email -> {
            val p = account.account.split("@")
            val name = if (p[0].length > 4)
                "${p[0].substring(0..1)}${"*".repeat(p[0].length - 4)}${p[0].substring(p[0].length - 2)}@${p[1]}"
            else
                "${"*".repeat(p[0].length)}${p[1]}"
            onAccountNameFound(prefix + (if (prefix.isNotEmpty()) "(${account.title}): " else "") + name)
        }
        AccountType.CardNumber -> {
            val name = if (account.account.length > 4)
                "${account.account.substring(0..1)}${"*".repeat(account.account.length - 4)}${account.account.substring(account.account.length - 2)}"
            else
                "*".repeat(account.account.length)
            onAccountNameFound(prefix + (if (prefix.isNotEmpty()) "(${account.title}): " else "") + name)
        }
        AccountType.PhoneNumber -> {
            val name = "${account.account.substring(0..2)} **** ${account.account.substring(account.account.length - 4)}"
            onAccountNameFound(prefix + (if (prefix.isNotEmpty()) "(${account.title}): " else "") + name)
        }
        AccountType.NickName -> {
            val name = account.account
            onAccountNameFound(prefix + (if (prefix.isNotEmpty()) "(${account.title}): " else "") + name)
        }
        AccountType.Reference -> {
            //引用类型代表使用第三方登录(如QQ),这里账户类别会是其余账号的id
            sharedViewModel.getAccountById(account.account.toInt()).collect {
                getDisplayAccount(it, sharedViewModel, "第三方", onAccountNameFound)
            }
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
    Surface(
        modifier = Modifier
            .padding(12.dp)
            .width(width), shape = MaterialTheme.shapes.medium.copy(CornerSize(12.dp)), color = AccountIconBackground.copy(0.5f)
    ) {
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
fun LargeAccountItemPreview() {
    LargeAccountItem(Account(0, "Microsoft", "", "panrunqiu@outlook.com", "abc********def", AccountType.Email)) {}
}