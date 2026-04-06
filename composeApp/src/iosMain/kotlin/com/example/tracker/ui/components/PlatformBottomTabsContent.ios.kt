package com.example.tracker.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
actual fun PlatformBottomTabsContent(
    modifier: Modifier
) {
    var selectedTabIndexThree by rememberSaveable { mutableIntStateOf(0) }
    var selectedTabIndexFour by rememberSaveable { mutableIntStateOf(0) }

    androidx.compose.foundation.layout.Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, androidx.compose.ui.Alignment.Bottom)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(3) { index ->
                Button(
                    onClick = { selectedTabIndexThree = index },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Tab ${index + 1}")
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(4) { index ->
                Button(
                    onClick = { selectedTabIndexFour = index },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Tab ${index + 1}")
                }
            }
        }
    }
}