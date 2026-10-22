package com.example.wickedlista

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ComposeFoundationFlags
import androidx.compose.foundation.ExperimentalFoundationApi
import com.example.wickedlista.ui.theme.WickedListaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint()
@OptIn(ExperimentalFoundationApi::class)
open class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        ComposeFoundationFlags.isInheritedTextStyleEnabled = true
       // installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            WickedListaTheme {
               WickedListaApp()
            }
        }
    }
}
