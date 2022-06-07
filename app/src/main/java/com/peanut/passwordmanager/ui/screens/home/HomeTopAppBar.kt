package com.peanut.passwordmanager.ui.screens.home

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.peanut.passwordmanager.R
import com.peanut.passwordmanager.server.HttpService
import com.peanut.passwordmanager.ui.component.BaseTopAppBar
import com.peanut.passwordmanager.ui.component.SearchAction
import com.peanut.passwordmanager.ui.component.ServerAction
import com.peanut.passwordmanager.ui.viewmodel.SharedViewModel
import com.peanut.passwordmanager.util.AdditionalFunctions.isPortAvailable
import com.peanut.passwordmanager.util.Constants
import com.peanut.passwordmanager.util.TopAppBarState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeTopAppBar(topAppBarState: TopAppBarState, sharedViewModel: SharedViewModel, scrollBehavior: TopAppBarScrollBehavior? = null) {
    var searchTextState by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    when (topAppBarState) {
        TopAppBarState.DEFAULT -> {
            DefaultHomeTopAppBar(scrollBehavior = scrollBehavior, onSearchClick = {
                coroutineScope.launch {
                    delay(200)
                    sharedViewModel.topAppBarState.value = TopAppBarState.SEARCH
                }
            }, onServerClicked = {
                if (!isPortAvailable(Constants.NETWORK_PORT)) {
                    Toast.makeText(context, "端口${Constants.NETWORK_PORT}被占用", Toast.LENGTH_SHORT).show()
                }else {
                    Toast.makeText(context, "开启局域网共享", Toast.LENGTH_SHORT).show()
                    context.startService(Intent(context, HttpService::class.java))
                }
            })
        }
        else -> {
            SearchHomeTopAppBar(text = searchTextState, scrollBehavior = scrollBehavior, onSearchClick = {
                println("onSearchClick")
                sharedViewModel.searchAccounts(it)
            }, onCloseClick = {
                coroutineScope.launch {
                    delay(200)
                    sharedViewModel.topAppBarState.value = TopAppBarState.DEFAULT
                }
            }, onTextChange = { searchTextState = it })
        }
    }
}

@Composable
fun DefaultHomeTopAppBar(scrollBehavior: TopAppBarScrollBehavior? = null, onSearchClick: () -> Unit, onServerClicked: () -> Unit) {
    BaseTopAppBar(
        title = {
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Peanut")
                    }
                    append(" ")
                    append("Pass")
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        actions = {
            SearchAction{ onSearchClick() }
            ServerAction { onServerClicked() }
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
fun SearchHomeTopAppBar(
    text: String,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onTextChange: (String) -> Unit,
    onCloseClick: () -> Unit,
    onSearchClick: (String) -> Unit
) {
    BaseTopAppBar(
        title = {
            TextField(
                value = text, onValueChange = { onTextChange(it) }, modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(text = "搜索名称、账户名、网站...", color = MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.medium))
                },
                singleLine = true, keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    onSearchClick(text)
                }),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.onSurface,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
            )
        },
        actions = {
            IconButton(onClick = {
                if (text.isNotEmpty()) {
                    onTextChange("")
                } else {
                    onCloseClick()
                }
            }) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = stringResource(id = R.string.close_search)
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = {}, modifier = Modifier.alpha(ContentAlpha.disabled)) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = stringResource(id = R.string.search_your_accounts)
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}




@Composable
@Preview
fun DefaultHomeTopAppBarPreview() {
    DefaultHomeTopAppBar(onSearchClick = {}, onServerClicked = {})
}

@Composable
@Preview
fun SearchHomeTopAppBarPreview() {
    SearchHomeTopAppBar("", onSearchClick = {}, onCloseClick = {}, onTextChange = {})
}