package com.folkmanis.laiks.ui.screens.user_settings.main_settings

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.folkmanis.laiks.R
import com.folkmanis.laiks.utilities.composables.DialogWithSaveAndCancel
import com.folkmanis.laiks.utilities.composables.PasswordField
import com.folkmanis.laiks.utilities.onetap.OneTapSignInWithGoogle
import com.folkmanis.laiks.utilities.onetap.rememberOneTapSignInState
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

private const val TAG = "ReAuthenticate"

@Composable
fun ReAuthenticate(
    onReAuthenticated: () -> Unit,
    onCancel: () -> Unit,
    user: FirebaseUser,
) {

    val googleLoginState = rememberOneTapSignInState()

    val scope = rememberCoroutineScope()

    val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        onCancel()
    }

    var passwordInputActive by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        when (user.getIdToken(true).await().signInProvider) {

            GoogleAuthProvider.PROVIDER_ID -> {
                googleLoginState.open()
            }

            EmailAuthProvider.PROVIDER_ID -> {
                passwordInputActive = true
            }
        }

    }

    if (passwordInputActive)
        PasswordInput(
            email = user.email ?: "",
            onCancel = onCancel,
            onPassword = { password ->
                val credential = EmailAuthProvider.getCredential(user.email!!, password)
                scope.launch(exceptionHandler) {
                    user.reauthenticate(credential).await()
                    onReAuthenticated()
                }
                passwordInputActive = false
            }
        )

    OneTapSignInWithGoogle(
        state = googleLoginState,
        clientId = stringResource(R.string.one_tap_web_client_id),
        onCredential = { signInCredential ->
            signInCredential.googleIdToken?.also { idToken ->
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                scope.launch(exceptionHandler) {
                    user.reauthenticate(credential).await()
                    onReAuthenticated()
                }
            }
        },
        onError = {
            Log.e(TAG, "${it.message}")
            onCancel()
        }
    )

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

@Preview
@Composable
fun PasswordInputPreview() {
    MaterialTheme {
        PasswordInput(
            email = "user@example.com",
            onCancel = { },
            onPassword = {}
        )
    }
}