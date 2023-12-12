package com.folkmanis.laiks.ui.screens.user_menu

import android.net.Uri
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.folkmanis.laiks.R
import com.folkmanis.laiks.utilities.composables.AvatarIcon

@Composable
fun UserMenuIconButton(
    modifier: Modifier = Modifier,
    photoUrl: Uri?,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        if (photoUrl == null) {
            Icon(
                imageVector = Icons.Outlined.AccountCircle,
                contentDescription = stringResource(R.string.expand_user_menu_button),
                modifier = Modifier.size(24.dp)
            )
        } else {
            AvatarIcon(
                photoUrl = photoUrl,
                contentDescription = stringResource(R.string.expand_user_menu_button),
                modifier = Modifier.size(30.dp)
            )
        }
    }
}