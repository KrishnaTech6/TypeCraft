package com.example.typecraft.ui.components


import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun StyleButton(
    onClick: () -> Unit,
    enabled: Boolean,
    isActive: Boolean = false,
    label: String,
    icon: ImageVector
) {
    TextButton(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
        )
    ) {
        Icon(imageVector = icon, contentDescription = label)
    }
}