package com.example.tracker.ui.glass

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.example.tracker.ui.blurglass.glass.GlassLayer
import com.example.tracker.ui.blurglass.glass.layerGlass
import com.example.tracker.ui.blurglass.glass.memoizedLayerGlass
import org.jetbrains.compose.resources.painterResource
import tracker.composeapp.generated.resources.Res
import tracker.composeapp.generated.resources.compose_multiplatform

val LocalPlatformGlassLayer = compositionLocalOf<GlassLayer?> { null }

@Composable
actual fun PlatformGlassScaffold(
    modifier: Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val glassLayer = memoizedLayerGlass()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(Res.drawable.compose_multiplatform),
            contentDescription = null,
            modifier = Modifier
                .layerGlass(glassLayer)
                .then(modifier)
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        CompositionLocalProvider(LocalPlatformGlassLayer provides glassLayer) {
            content()
        }
    }
}
