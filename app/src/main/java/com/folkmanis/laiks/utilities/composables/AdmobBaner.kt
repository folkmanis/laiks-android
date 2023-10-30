package com.folkmanis.laiks.utilities.composables

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.viewinterop.AndroidView
import com.folkmanis.laiks.BuildConfig
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun AdmobBanner(
    modifier: Modifier = Modifier,
    onSetHeight: (Int) -> Unit,
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    AndroidView(
        factory = { context ->
            AdView(context).apply {
                val adSize =
                    AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, screenWidth)
                setAdSize(adSize)
                onSetHeight(adSize.height)
                adUnitId = BuildConfig.AD_UNIT_ID
                loadAd(AdRequest.Builder().build())
            }
        },
        modifier = modifier.background(color = Color.DarkGray),
    )
}