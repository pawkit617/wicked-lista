package com.example.wickedlista

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.wickedlista.ui.theme.WickedListaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint()
open class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("CURT", "MainActivity : onCreate")
        enableEdgeToEdge()
        setContent {
            WickedListaTheme {
               WickedListaApp()
            }
        }
    }
}
