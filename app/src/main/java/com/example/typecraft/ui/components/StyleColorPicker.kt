package com.example.typecraft.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StyleColorPicker(
    onColorSelected: (Color) -> Unit,
    enabled: Boolean = true,
    currentColor: Color = Color.Black
) {
    val colors = listOf(
        Color.Black, Color.DarkGray, Color.Gray, Color.LightGray,
        Color.Red, Color.Green, Color.Blue, Color.Yellow,
        Color.Magenta, Color.Cyan, Color(0xFF9C27B0), Color(0xFF2196F3),
        Color(0xFFE91E63), Color(0xFF4CAF50), Color(0xFFFF9800)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        colors.forEach { color ->
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(color)
                    .clickable(enabled = enabled) { onColorSelected(color) }
                    .border(3.dp, if (color == currentColor) Color.White else Color.Transparent, CircleShape)
                    .padding(if (color == currentColor) 2.dp else 0.dp)
            )
        }
    }
}