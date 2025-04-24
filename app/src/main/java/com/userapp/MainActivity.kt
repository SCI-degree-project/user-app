package com.userapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.userapp.ui.CatalogScreen
import com.userapp.ui.theme.UserappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UserappTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CatalogScreen(modifier = Modifier.padding(innerPadding))
                }

            }
        }
    }
}
