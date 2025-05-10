package com.example.typecraft

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.typecraft.ui.components.SimpleTextEditor
import com.example.typecraft.ui.theme.TypeCraftTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TypeCraftTheme {
                TypeCraftApp()
            }
        }
    }
}

@Composable
fun TypeCraftApp() {
    Surface(
        modifier = Modifier.fillMaxSize().safeContentPadding(),
        color = MaterialTheme.colorScheme.background
    ) {
        SimpleTextEditor()
    }
}