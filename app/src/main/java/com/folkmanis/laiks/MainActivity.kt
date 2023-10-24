package com.folkmanis.laiks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import com.folkmanis.laiks.ui.screens.laiks.LaiksAppScreen
import com.folkmanis.laiks.ui.theme.LaiksTheme
import com.folkmanis.laiks.utilities.composables.AdmobBanner
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this) {}

        setContent {
            LaiksTheme {
                val windowSize = calculateWindowSizeClass(activity = this)
                Surface {
                    Column(
                            modifier = Modifier.fillMaxSize()
                    ) {

                        LaiksAppScreen(
                                windowSize = windowSize,
                                modifier = Modifier.fillMaxHeight()
                        )
                        AdmobBanner()
                    }
                }
            }
        }
    }

}
