package com.folkmanis.laiks.utilities.oauth

import android.content.Intent
import com.firebase.ui.auth.AuthUI
import com.folkmanis.laiks.R

fun getSignInIntent(): Intent {
    val providers = arrayListOf(
        AuthUI.IdpConfig.GoogleBuilder().build(),
        AuthUI.IdpConfig.EmailBuilder().build(),
    )

    return AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setAvailableProviders(providers)
        .setLogo(R.drawable.clock_clock)
        .build()

}