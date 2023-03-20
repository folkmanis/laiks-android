package com.folkmanis.laiks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.folkmanis.laiks.ui.ClockViewModel
import com.folkmanis.laiks.ui.theme.LaiksTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LaiksTheme {
                LaiksApp()
            }
        }
    }

}
