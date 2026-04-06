package com.example.tracker.ui.blurglass.utils

import android.os.Build
import android.graphics.RenderEffect
import com.example.tracker.ui.blurglass.BlurGlassEffect

fun BlurGlassEffect.effect(effect: RenderEffect) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return

    val currentEffect = renderEffect
    renderEffect =
        if (currentEffect != null) {
            RenderEffect.createChainEffect(effect, currentEffect)
        } else {
            effect
        }
}

fun BlurGlassEffect.effect(effect: androidx.compose.ui.graphics.RenderEffect) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return
    effect(effect.asAndroidRenderEffect())
}