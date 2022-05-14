package com.peanut.passwordmanager.ui.component

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.peanut.passwordmanager.R
import com.peanut.passwordmanager.util.TopAppBarState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeTopAppBar(scrollBehavior: TopAppBarScrollBehavior? = null) {
    var topAppBarState by remember { mutableStateOf(TopAppBarState.DEFAULT) }
    var searchTextState by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    when (topAppBarState) {
        TopAppBarState.DEFAULT -> {
            DefaultHomeTopAppBar(scrollBehavior = scrollBehavior, onSearchClick = {
                coroutineScope.launch {
                    delay(200)
                    topAppBarState = TopAppBarState.SEARCH
                }
            })
        }
        TopAppBarState.SEARCH -> {
            SearchHomeTopAppBar(text = searchTextState, scrollBehavior = scrollBehavior, onSearchClick = {}, onCloseClick = {
                coroutineScope.launch {
                    delay(200)
                    topAppBarState = TopAppBarState.DEFAULT
                }
            }, onTextChange = { searchTextState = it })
        }
    }
}

@Composable
fun DefaultHomeTopAppBar(scrollBehavior: TopAppBarScrollBehavior? = null, onSearchClick: () -> Unit) {
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
            IconButton(onClick = { onSearchClick() }) {
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
fun BaseTopAppBar(
    title: @Composable () -> Unit = {},
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    val backgroundColors = TopAppBarDefaults.smallTopAppBarColors()
    val backgroundColor = backgroundColors.containerColor(
        scrollFraction = scrollBehavior?.scrollFraction ?: 0f
    ).value
    val foregroundColors = TopAppBarDefaults.smallTopAppBarColors(
        containerColor = Color.Transparent,
        scrolledContainerColor = Color.Transparent
    )
    // Wrapping in a Surface to handle window insets https://issuetracker.google.com/issues/183161866
    Surface(color = backgroundColor) {
        SmallTopAppBar(
            title = title,
            actions = actions,
            navigationIcon = navigationIcon,
            scrollBehavior = scrollBehavior,
            colors = foregroundColors,
            modifier = Modifier.windowInsetsPadding(
                WindowInsets.safeDrawing.only(
                    WindowInsetsSides.Horizontal + WindowInsetsSides.Top
                )
            )
        )
    }
}


@Composable
@Preview
fun DefaultHomeTopAppBarPreview() {
    DefaultHomeTopAppBar(onSearchClick = {})
}

@Composable
@Preview
fun SearchHomeTopAppBarPreview() {
    SearchHomeTopAppBar("", onSearchClick = {}, onCloseClick = {}, onTextChange = {})
}