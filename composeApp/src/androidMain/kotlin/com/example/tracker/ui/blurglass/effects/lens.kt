package com.example.tracker.ui.blurglass.effects

import androidx.compose.ui.util.fastCoerceAtLeast

import com.example.tracker.ui.blurglass.BlurGlassEffect

fun BlurGlassEffect.lens(
    refractionHeight: Float,
    refractionAmount: Float,
    depth: Boolean = false,
    chromaticAberration: Boolean = false
) {
    if (refractionHeight <= 0f || refractionAmount <= 0f) return

    if (padding > 0f) {
        padding = (padding - refractionHeight).fastCoerceAtLeast(0f)
    }
}


