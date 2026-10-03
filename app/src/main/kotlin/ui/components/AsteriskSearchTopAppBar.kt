// Copyright 2026, AsteriskMETA contributors
// SPDX-License-Identifier: GPL-3.0

package ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import app.R
import ui.layout.pageHorizontalPadding
import ui.theme.AsteriskMotion
import ui.icons.AsteriskIcons as Icons

@Composable
internal fun AsteriskSearchTopAppBar(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null,
    searchAvailable: Boolean = true,
    searchField: @Composable (Modifier) -> Unit = { fieldModifier ->
        AsteriskSearchField(
            query = query,
            onQueryChange = onQueryChange,
            placeholder = placeholder,
            clearContentDescription = stringResource(R.string.common_clear),
            modifier = fieldModifier,
        )
    },
) {
    var searchActive by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    fun closeSearch() {
        searchActive = false
        onQueryChange("")
        focusManager.clearFocus()
        keyboardController?.hide()
    }

    LaunchedEffect(searchAvailable) {
        if (!searchAvailable && searchActive) closeSearch()
    }

    // Outgoing content remains composed during the fade, but must not handle back events.
    if (searchActive && searchAvailable) {
        val backState = rememberNavigationEventState(NavigationEventInfo.None)
        NavigationBackHandler(
            state = backState,
            isBackEnabled = true,
            onBackCompleted = { closeSearch() },
        )
    }

    AsteriskSearchTopBarTransition(searchActive = searchActive && searchAvailable) { showSearch ->
        if (showSearch) {
            val focusRequester = remember { FocusRequester() }
            LaunchedEffect(searchActive, searchAvailable) {
                if (searchActive && searchAvailable) focusRequester.requestFocus()
            }
            AsteriskTopAppBar(
                modifier = modifier,
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = { closeSearch() }) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBack,
                            stringResource(R.string.common_back),
                        )
                    }
                },
                title = {
                    searchField(Modifier.fillMaxWidth().focusRequester(focusRequester))
                },
            )
        } else {
            AsteriskTopAppBar(
                title = title,
                modifier = modifier,
                navigationIcon = navigationIcon,
                scrollBehavior = scrollBehavior,
                actions = {
                    if (searchAvailable) {
                        IconButton(onClick = { searchActive = true }) {
                            Icon(Icons.Rounded.Search, stringResource(R.string.common_search))
                        }
                    }
                    actions()
                },
            )
        }
    }
}

@Composable
private fun AsteriskSearchTopBarTransition(
    searchActive: Boolean,
    content: @Composable (Boolean) -> Unit,
) {
    val effectsSpec = AsteriskMotion.fastEffects<Float>()
    val fadeTransition = AsteriskMotion.fadeThrough<Boolean>(effectsSpec)
    AnimatedContent(
        targetState = searchActive,
        transitionSpec = { fadeTransition().using(null) },
        label = "search-top-bar",
    ) { showSearch ->
        content(showSearch)
    }
}

@Composable
internal fun AsteriskTopBarControls(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .pageHorizontalPadding()
            .padding(bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        content = content,
    )
}
