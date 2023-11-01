package com.folkmanis.laiks.utilities.composables

import android.net.Uri
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun AvatarIcon(
    photoUrl: Uri,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(photoUrl)
            .build(),
        contentDescription = contentDescription,
        contentScale = ContentScale.FillBounds,
        modifier = modifier
            .clip(shape = CircleShape)
    )
}
