package com.folkmanis.laiks.utilities.onetap

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.folkmanis.laiks.R
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes

@Composable
fun rememberOneTapSignInState(): OneTapSigninState {
    return remember {
        OneTapSigninState()
    }
}

const val TAG = "OneTapSignInWithGoogle"

@Composable
fun OneTapSignInWithGoogle(
    state: OneTapSigninState,
    clientId: String,
    rememberAccount: Boolean = true,
    nonce: String? = null,
    onDialogDismissed: (message: Int) -> Unit = {},
    onError: (Throwable) -> Unit,
    onCredential: (SignInCredential) -> Unit,
) {
    val context: Context = LocalContext.current
    val activityLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
    ) { result ->
        try {
            if (result.resultCode == Activity.RESULT_OK) {
                val oneTapClient = Identity.getSignInClient(context)
                val credentials = oneTapClient.getSignInCredentialFromIntent(result.data)
                state.close()
                onCredential(credentials)
            }
        } catch (e: ApiException) {
            Log.e(TAG, "${e.message}")
            when (e.statusCode) {
                CommonStatusCodes.CANCELED ->
                    onDialogDismissed(R.string.one_tap_dialog_canceled)

                CommonStatusCodes.NETWORK_ERROR ->
                    onDialogDismissed(R.string.one_tap_network_error)

                else -> onError(e)

            }
        }
        state.close()
    }

    LaunchedEffect(state.opened) {
        if (state.opened) {
            signIn(
                context = context,
                clientId = clientId,
                rememberAccount = rememberAccount,
                nonce = nonce,
                launcherActivityResult = { intentSenderRequest ->
                    activityLauncher.launch(intentSenderRequest)
                },
                onError = {
                    onError(it)
                    state.close()
                }
            )
        }
    }
}

private fun signIn(
    context: Context,
    clientId: String,
    rememberAccount: Boolean,
    nonce: String?,
    launcherActivityResult: (IntentSenderRequest) -> Unit,
    onError: (Throwable) -> Unit,
) {
    val oneTapClient = Identity.getSignInClient(context)
    val signInRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setNonce(nonce)
                .setServerClientId(clientId)
                .setFilterByAuthorizedAccounts(rememberAccount)
                .build()
        )
        .setAutoSelectEnabled(true)
        .build()

    oneTapClient.beginSignIn(signInRequest)
        .addOnSuccessListener { result ->
            try {
                launcherActivityResult(
                    IntentSenderRequest.Builder(
                        result.pendingIntent.intentSender
                    ).build()
                )
            } catch (e: Exception) {
                onError(e)
                Log.e(TAG, "${e.message}")
            }
        }
        .addOnFailureListener {
            signUp(
                context = context,
                clientId = clientId,
                nonce = nonce,
                launchedActivityResult = launcherActivityResult,
                onError = onError,
            )
            Log.e(TAG, "${it.message}")
        }
}

private fun signUp(
    context: Context,
    clientId: String,
    nonce: String?,
    launchedActivityResult: (IntentSenderRequest) -> Unit,
    onError: (Throwable) -> Unit,
) {
    val oneTapClient = Identity.getSignInClient(context)
    val signInRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setNonce(nonce)
                .setServerClientId(clientId)
                .setFilterByAuthorizedAccounts(false)
                .build()
        )
        .build()

    oneTapClient.beginSignIn(signInRequest)
        .addOnSuccessListener { result ->
            try {
                launchedActivityResult(
                    IntentSenderRequest.Builder(
                        result.pendingIntent.intentSender
                    ).build()
                )
            } catch (e: Exception) {
                onError(e)
                Log.e(TAG, "${e.message}")
            }
        }
        .addOnFailureListener {
            onError(OneTapGoogleAccountNotFoundError())
            Log.e(TAG, "${it.message}")
        }
}