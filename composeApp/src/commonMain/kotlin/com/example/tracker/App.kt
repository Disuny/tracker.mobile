package com.example.tracker

import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.tracker.ui.components.PlatformBottomTabsContent
import com.example.tracker.ui.glass.PlatformGlassScaffold

@Composable
@Preview
fun App() {
    MaterialTheme {
        PlatformGlassScaffold {
            PlatformBottomTabsContent(
                modifier = Modifier.safeContentPadding()
            )
        }
    }
}