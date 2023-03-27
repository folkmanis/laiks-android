package com.folkmanis.laiks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.folkmanis.laiks.ui.screens.laiks.LaiksAppScreen
import com.folkmanis.laiks.ui.theme.LaiksTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LaiksTheme(
                dynamicColor = false,
            ) {
                LaiksAppScreen()
            }
        }
    }

}
