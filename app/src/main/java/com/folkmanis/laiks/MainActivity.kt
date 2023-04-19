package com.folkmanis.laiks

import android.os.Bundle
import android.os.StrictMode
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import com.folkmanis.laiks.ui.theme.LaiksTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//       val policy= StrictMode.ThreadPolicy.Builder().permitNetwork().build()
//        StrictMode.setThreadPolicy(policy)
        setContent {
            LaiksTheme {
                Surface {
                    LaiksAppScreen()
                }
            }
        }
    }

}
