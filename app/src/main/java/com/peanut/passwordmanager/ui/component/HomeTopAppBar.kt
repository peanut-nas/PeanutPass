package com.peanut.passwordmanager.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.peanut.passwordmanager.R

@Composable
fun HomeTopAppBar(scrollBehavior: TopAppBarScrollBehavior? = null) {
    DefaultHomeTopAppBar(scrollBehavior = scrollBehavior, onSearch = {})
}

@Composable
fun DefaultHomeTopAppBar(scrollBehavior: TopAppBarScrollBehavior? = null, onSearch: () -> Unit) {
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
        showBackNavigationIcon = false,
        scrollBehavior = scrollBehavior,
        onSearchClick = onSearch
    )
}

@Composable
fun BaseTopAppBar(
    title: @Composable () -> Unit = {},
    showBackNavigationIcon: Boolean = false,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onBackClick: () -> Unit = {},
    onSearchClick: () -> Unit = {}
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
            actions = {
                IconButton(onClick = { onSearchClick() }) {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = stringResource(id = R.string.search_your_accounts)
                    )
                }
            },
            navigationIcon = {
                if (showBackNavigationIcon) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = stringResource(id = R.string.go_back)
                        )
                    }
                }
            },
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
    HomeTopAppBar()
}