package com.folkmanis.laiks.utilities.oauth

import android.content.Intent
import com.firebase.ui.auth.AuthUI
import com.folkmanis.laiks.R

fun getGoogleSignInIntent(): Intent {
    val providers = arrayListOf(
        AuthUI.IdpConfig
            .GoogleBuilder()
            .build(),
//        AuthUI.IdpConfig
//            .EmailBuilder()
//            .setAllowNewAccounts(true)
//            .setRequireName(true)
//            .build()
    )

    return AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setLogo(R.drawable.clock_clock)
        .setAvailableProviders(providers)
        .build()

}