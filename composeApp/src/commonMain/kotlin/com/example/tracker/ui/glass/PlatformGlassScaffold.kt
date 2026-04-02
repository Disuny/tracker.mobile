package com.example.tracker.ui.glass

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun PlatformGlassScaffold(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
)
