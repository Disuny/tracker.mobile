package com.example.tracker.ui.blurglass.effects

import android.graphics.RenderEffect
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.toAndroidTileMode
import com.example.tracker.ui.blurglass.BlurGlassEffect

fun BlurGlassEffect.blur(
    radius: Float,
    edge: TileMode = TileMode.Clamp
) {
    if (radius <= 0f) return

    if (edge != TileMode.Clamp || renderEffect != null) {
        if (radius > padding) {
            padding = radius
        }
    }

    val currentEffect = renderEffect
    renderEffect =
        if (currentEffect != null) {
            RenderEffect.createBlurEffect(
                radius,
                radius,
                currentEffect,
                edge.toAndroidTileMode()
            )
        } else {
            RenderEffect.createBlurEffect(
                radius,
                radius,
                edge.toAndroidTileMode()
            )
        }
}