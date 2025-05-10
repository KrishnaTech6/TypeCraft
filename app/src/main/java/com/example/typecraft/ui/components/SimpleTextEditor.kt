package com.example.typecraft.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.example.typecraft.model.TextInfo
import com.example.typecraft.model.TextStyle

@Composable
fun SimpleTextEditor() {
    var texts by remember { mutableStateOf(listOf<TextInfo>()) }
    var newText by remember { mutableStateOf("") }
    var editingIndex by remember { mutableStateOf<Int?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }
    var currentEditText by remember { mutableStateOf("") }
    var history by remember { mutableStateOf(mutableListOf(listOf<TextInfo>())) }
    var historyIndex by remember { mutableStateOf(0) }
    var selectedFont by remember { mutableStateOf("Default") }
    var isFontMenuExpanded by remember { mutableStateOf(false) }

    val availableFonts = listOf(
        "Default" to FontFamily.Default,
        "Sans Serif" to FontFamily.SansSerif,
        "Serif" to FontFamily.Serif,
        "Monospace" to FontFamily.Monospace,
        "Cursive" to FontFamily.Cursive
    )

    fun saveToHistory(newTexts: List<TextInfo>) {
        // Don't save if identical to current state
        if (texts == newTexts) return

        // Clear any future history if we're not at the end
        val newHistory = history.take(historyIndex + 1).toMutableList()
        // Add the new state
        newHistory.add(newTexts)
        history = newHistory
        historyIndex = newHistory.size - 1
        texts = newTexts
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5))
                .padding(16.dp)
                .border(1.dp, Color.Gray)
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        // Deselect on tap in empty area
                        editingIndex = null
                    }
                }
        ) {
            // Display all texts
            texts.forEachIndexed { index, textInfo ->
                DraggableTextItem(
                    text = textInfo.text,
                    position = textInfo.position,
                    textStyle = textInfo.style,
                    isSelected = index == editingIndex,
                    onSelect = { editingIndex = index },
                    onPositionChange = { newPos ->
                        saveToHistory(
                            texts.mapIndexed { i, text ->
                                if (i == index) {
                                    text.copy(position = newPos)
                                } else {
                                    text
                                }
                            }
                        )
                    },
                    onDoubleClick = {
                        showEditDialog = true
                        currentEditText = textInfo.text
                    }
                )
            }
            if (showEditDialog) {
                EditDialog(
                    text = currentEditText,
                    onConfirm = { newText ->
                        saveToHistory(
                            texts.mapIndexed { i, text ->
                                if (i == editingIndex!!) {
                                    text.copy(text = newText)
                                } else {
                                    text
                                }
                            }
                        )
                        showEditDialog = false
                    },
                    onCancel = { showEditDialog = false }
                )
            }
        }

        // Controls
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            TextField(
                value = newText,
                onValueChange = { newText = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Enter text") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        if (newText.isNotEmpty()) {
                            saveToHistory(
                                texts + TextInfo(
                                    text = newText,
                                    position = Offset(100f, 100f),
                                    style = TextStyle()
                                )
                            )
                            newText = ""
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Add Text")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        if (historyIndex > 0) {
                            historyIndex--
                            texts = history[historyIndex]
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Undo")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        if (historyIndex < history.size - 1) {
                            historyIndex++
                            texts = history[historyIndex]
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Redo")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Style editing panel - redesigned for better UX
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        "Text Styling",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Font family selector
                    Button(
                        onClick = { isFontMenuExpanded = !isFontMenuExpanded },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = editingIndex != null,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Font: $selectedFont")
                        }
                    }

                    DropdownMenu(
                        expanded = isFontMenuExpanded,
                        onDismissRequest = { isFontMenuExpanded = false },
                        modifier = Modifier.width(200.dp)
                    ) {
                        availableFonts.forEach { (font, fontFamily) ->
                            DropdownMenuItem(
                                text = { Text(font, fontFamily = fontFamily) },
                                onClick = {
                                    selectedFont = font
                                    editingIndex?.let { index ->
                                        saveToHistory(
                                            texts.mapIndexed { i, text ->
                                                if (i == index) {
                                                    text.copy(style = text.style.copy(fontFamily = fontFamily))
                                                } else {
                                                    text
                                                }
                                            }
                                        )
                                    }
                                    isFontMenuExpanded = false
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Text style controls in a row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Bold button
                        StyleButton(
                            onClick = {
                                editingIndex?.let { index ->
                                    saveToHistory(
                                        texts.mapIndexed { i, text ->
                                            if (i == index) {
                                                text.copy(style = text.style.copy(isBold = !text.style.isBold))
                                            } else {
                                                text
                                            }
                                        }
                                    )
                                }
                            },
                            enabled = editingIndex != null,
                            isActive = editingIndex != null && texts.getOrNull(editingIndex!!)?.style?.isBold == true,
                            label = "B"
                        )

                        // Italic button
                        StyleButton(
                            onClick = {
                                editingIndex?.let { index ->
                                    saveToHistory(
                                        texts.mapIndexed { i, text ->
                                            if (i == index) {
                                                text.copy(style = text.style.copy(isItalic = !text.style.isItalic))
                                            } else {
                                                text
                                            }
                                        }
                                    )
                                }
                            },
                            enabled = editingIndex != null,
                            isActive = editingIndex != null && texts.getOrNull(editingIndex!!)?.style?.isItalic == true,
                            label = "I"
                        )

                        // Size decrease button
                        StyleButton(
                            onClick = {
                                editingIndex?.let { index ->
                                    if (texts.getOrNull(index)?.style?.fontSize ?: 0 > 8) {
                                        saveToHistory(
                                            texts.mapIndexed { i, text ->
                                                if (i == index) {
                                                    text.copy(style = text.style.copy(fontSize = text.style.fontSize - 1))
                                                } else {
                                                    text
                                                }
                                            }
                                        )
                                    }
                                }
                            },
                            enabled = editingIndex?.let { index ->
                                texts.getOrNull(index)?.style?.fontSize ?: 0 > 8
                            } ?: false,
                            label = "A-"
                        )

                        // Size increase button
                        StyleButton(
                            onClick = {
                                editingIndex?.let { index ->
                                    saveToHistory(
                                        texts.mapIndexed { i, text ->
                                            if (i == index) {
                                                text.copy(style = text.style.copy(fontSize = text.style.fontSize + 1))
                                            } else {
                                                text
                                            }
                                        }
                                    )
                                }
                            },
                            enabled = editingIndex != null,
                            label = "A+"
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Color picker with better styling
                    Text(
                        "Color",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    StyleColorPicker(
                        onColorSelected = { color ->
                            editingIndex?.let { index ->
                                saveToHistory(
                                    texts.mapIndexed { i, text ->
                                        if (i == index) {
                                            text.copy(style = text.style.copy(color = color))
                                        } else {
                                            text
                                        }
                                    }
                                )
                            }
                        },
                        enabled = editingIndex != null,
                        currentColor = editingIndex?.let { texts.getOrNull(it)?.style?.color } ?: Color.Black
                    )
                }
            }
        }
    }
}