package com.folkmanis.laiks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.folkmanis.laiks.ui.screens.laiks.LaiksAppScreen
import com.folkmanis.laiks.ui.theme.LaiksTheme
import com.folkmanis.laiks.utilities.composables.AdmobBanner
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this) {}

        setContent {
            var adHeight by remember {
                mutableStateOf(50)
            }
            LaiksTheme {
                val windowSize = calculateWindowSizeClass(activity = this)
                Surface {

                    Box {

                        LaiksAppScreen(
                            windowSize = windowSize,
                            modifier = Modifier
                                .padding(bottom = adHeight.dp)
                        )

                        AdmobBanner(
                            onSetHeight = { adHeight = it },
                            modifier = Modifier
                                .align(Alignment.BottomCenter),
                        )
                    }
                }
            }
        }
    }

}
