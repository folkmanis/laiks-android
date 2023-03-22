@file:OptIn(ExperimentalMaterial3Api::class)

package com.folkmanis.laiks.ui.screens.laiks

import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.firebase.ui.auth.AuthUI
import com.folkmanis.laiks.R
import com.folkmanis.laiks.ui.theme.LaiksTheme
import com.google.firebase.auth.FirebaseUser

@Composable
fun LaiksTopBar(
    currentScreen: LaiksScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    state: LaiksUiState,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    TopAppBar(
        title = { Text(stringResource(id = currentScreen.title)) },
        actions = {
            if (state is LaiksUiState.LoggedIn) {
                AuthorizedUserMenuButton(
                    user = state.firebaseUser,
                    logout = {
                        AuthUI.getInstance()
                            .signOut(context)
                    },
                )
            } else {
                SignInButton()
            }
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        },
        modifier = modifier
    )
}

@Composable
fun SignInButton() {

    val context = LocalContext.current

    val providers = arrayListOf(
        AuthUI.IdpConfig.GoogleBuilder().build()
    )

    val signInIntent = AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setAvailableProviders(providers)
        .build()

    IconButton(
        onClick = {
            context.startActivity(signInIntent)
        }
    ) {
        Icon(
            imageVector = Icons.Outlined.AccountCircle,
            contentDescription = stringResource(id = R.string.login_button)
        )
    }
}

@Composable
fun AuthorizedUserMenuButton(
    user: FirebaseUser,
    logout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (user.photoUrl != null) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(user.photoUrl)
                .build(),
            contentDescription = user.displayName,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .clip(CircleShape)
                .clickable { logout() }
        )
    } else {
        Text(
            text = user.displayName ?: "Unknown",
            modifier = modifier
                .clickable { logout() }
        )
    }
}

@Preview
@Composable
fun LaiksTopBarPreview() {
    LaiksTheme {
        LaiksTopBar(
            currentScreen = LaiksScreen.Clock,
            canNavigateBack = true,
            navigateUp = { },
            state = LaiksUiState.NotLoggedIn,
        )
    }
}