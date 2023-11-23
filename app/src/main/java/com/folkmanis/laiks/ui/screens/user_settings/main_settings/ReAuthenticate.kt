package com.folkmanis.laiks.ui.screens.user_settings.main_settings

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.folkmanis.laiks.R
import com.folkmanis.laiks.utilities.composables.DialogWithSaveAndCancel
import com.folkmanis.laiks.utilities.composables.PasswordField
import com.folkmanis.laiks.utilities.oauth.getGoogleSignInIntent
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun ReAuthenticate(
    onReAuthenticated: () -> Unit,
    onCancel: () -> Unit,
    user: FirebaseUser,
) {

    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    var passwordInputActive by remember {
        mutableStateOf(false)
    }

    val googleLauncher = googleLoginLauncher(
        onResultOk = onReAuthenticated,
        onCancel = onCancel
    )

    LaunchedEffect(Unit) {
        when (user.getIdToken(true).await().signInProvider) {
            GoogleAuthProvider.PROVIDER_ID -> {
                googleLauncher.launch(getGoogleSignInIntent())
            }

            EmailAuthProvider.PROVIDER_ID -> {
                passwordInputActive = true
            }
        }
//    if (token.signInProvider == GoogleAuthProvider.PROVIDER_ID) {

    }

    if (passwordInputActive)
        PasswordInput(
            email = user.email ?: "",
            onCancel = onCancel,
            onPassword = { password ->
                val credential = EmailAuthProvider.getCredential(user.email!!, password)
                passwordInputActive = false
                scope.launch(
                    CoroutineExceptionHandler { _, _ -> onCancel() }
                ) {
                    user.reauthenticate(credential).await()
                }
            }
        )

}

@Composable
fun googleLoginLauncher(
    onResultOk: () -> Unit,
    onCancel: () -> Unit,
): ManagedActivityResultLauncher<Intent, FirebaseAuthUIAuthenticationResult> {
    return rememberLauncherForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK)
            onResultOk()
        else
            onCancel()
    }

}

@Composable
fun PasswordInput(
    email: String,
    onCancel: () -> Unit,
    onPassword: (String) -> Unit,
    modifier: Modifier = Modifier,
) {

    var password by remember {
        mutableStateOf("")
    }

    Dialog(
        onDismissRequest = onCancel
    ) {
        DialogWithSaveAndCancel(
            onCancel = onCancel,
            onSave = { onPassword(password) },
            headingText = R.string.password_input_dialog_title,
            modifier = modifier
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = stringResource(R.string.password_input_dialog_text, email))
                Spacer(modifier = Modifier.height(16.dp))
                PasswordField(value = password, onValueChange = { password = it })
            }
        }
    }
}