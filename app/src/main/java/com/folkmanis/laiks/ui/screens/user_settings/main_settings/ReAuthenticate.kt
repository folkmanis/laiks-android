package com.folkmanis.laiks.ui.screens.user_settings.main_settings

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

@Composable
fun ReAuthenticate() {

    val context = LocalContext.current

    val loginLauncher = rememberLauncherForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
//            viewModel.deleteAccount(onUserDeleted)
        }
    }


//    val token = user.getIdToken(true).await()
//    Log.d(UserSettingsViewModel.TAG, "Provider: ${token?.signInProvider}")
//    if (token.signInProvider == GoogleAuthProvider.PROVIDER_ID) {

}