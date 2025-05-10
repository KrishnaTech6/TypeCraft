package com.example.typecraft.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily

data class TextInfo(
    val text: String,
    val position: Offset,
    val style: TextStyle = TextStyle()
)

data class TextStyle(
    val isBold: Boolean = false,
    val fontSize: Int = 12,
    val color: Color = Color.Black,
    val isItalic: Boolean = false,
    val fontFamily: FontFamily = FontFamily.Default
)