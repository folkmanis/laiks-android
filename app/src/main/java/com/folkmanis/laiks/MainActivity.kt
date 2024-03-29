package com.folkmanis.laiks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.folkmanis.laiks.ui.screens.laiks.LaiksAppNavigation
import com.folkmanis.laiks.ui.theme.LaiksTheme
import com.folkmanis.laiks.utilities.composables.AdmobBanner
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        
        MobileAds.initialize(this) {}

        setContent {
            var adHeight by remember {
                mutableIntStateOf(50)
            }
            LaiksTheme {
                val windowSize = calculateWindowSizeClass(activity = this)
                Surface {

                    Box {

                        LaiksAppNavigation(
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
