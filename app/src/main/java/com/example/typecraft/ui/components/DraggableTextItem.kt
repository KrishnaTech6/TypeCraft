package com.example.typecraft.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.typecraft.model.TextStyle
import kotlin.math.roundToInt

@Composable
fun DraggableTextItem(
    text: String,
    position: Offset,
    textStyle: TextStyle,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onPositionChange: (Offset) -> Unit,
    onDoubleClick: () -> Unit
) {
    val offsetState = remember { mutableStateOf(position) }
    var isDragging by remember { mutableStateOf(false) }

    // Update the position if it changes from outside
    LaunchedEffect(position) {
        offsetState.value = position
    }

    Box(
        modifier = Modifier
            .offset { IntOffset(offsetState.value.x.roundToInt(), offsetState.value.y.roundToInt()) }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        isDragging = true
                        onSelect()
                    },
                    onDragEnd = {
                        isDragging = false
                        // Notify parent of final position
                        onPositionChange(offsetState.value)
                    },
                    onDragCancel = { isDragging = false },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        // Just update local state during the drag for smoothness
                        offsetState.value = Offset(
                            offsetState.value.x + dragAmount.x,
                            offsetState.value.y + dragAmount.y
                        )
                    }
                )
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onSelect() },
                    onDoubleTap = { onDoubleClick() }
                )
            }
    ) {
        Text(
            text = text,
            modifier = Modifier
                .background(if (isSelected) Color.LightGray else Color.Transparent)
                .padding(8.dp),
            fontSize = textStyle.fontSize.sp,
            fontWeight = if (textStyle.isBold) FontWeight.Bold else FontWeight.Normal,
            fontStyle = if (textStyle.isItalic) FontStyle.Italic else FontStyle.Normal,
            fontFamily = textStyle.fontFamily,
            color = textStyle.color
        )
    }
}