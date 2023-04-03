package com.folkmanis.laiks.utilities.oauth

import android.content.Intent
import com.firebase.ui.auth.AuthUI

fun getSignInIntent(): Intent {
    val providers = arrayListOf(
        AuthUI.IdpConfig.GoogleBuilder().build()
    )

    return AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setAvailableProviders(providers)
        .build()

}