package com.example.tracker.ui.blurglass.highlight

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class Highlight(
    val width: Dp = 0.5f.dp,
    val blurRadius: Dp = width / 2f,
    val alpha: Float = 1f,
    val style: HighlightStyle = HighlightStyle.Default
) {

    companion object {
        @Stable
        val Base: Highlight = Highlight(style = HighlightStyle.Base)
        @Stable
        val Default: Highlight = Highlight()

//        @Stable
//        val Ambient: Highlight = Highlight(style = HighlightStyle.Ambient)
//
    }
}