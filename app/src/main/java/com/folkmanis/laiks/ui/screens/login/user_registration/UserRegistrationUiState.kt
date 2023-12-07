package com.folkmanis.laiks.ui.screens.login.user_registration

import androidx.annotation.StringRes
import com.folkmanis.laiks.R
import com.folkmanis.laiks.utilities.ext.isStrongPassword
import com.folkmanis.laiks.utilities.ext.isValidEmail

data class UserRegistrationUiState(
    val email: String = "",
    val emailPristine: Boolean = true,
    val password: String = "",
    val passwordPristine: Boolean = true,
    val passwordRepeat: String = "",
    val passwordRepeatPristine: Boolean = true,
    val displayName: String = "",
    val displayNamePristine: Boolean = true,
    val isBusy: Boolean = false,
) {
    val isValid: Boolean
        get() = email.isNotEmpty()
                && email.isValidEmail()
                && password.isNotEmpty()
                && password.isStrongPassword()
                && password == passwordRepeat
                && displayName.isNotEmpty()

    val emailError: (@StringRes Int)?
        get() = if (emailPristine || email.isValidEmail()) null else R.string.email_input_error

    val passwordError: (@StringRes Int)?
        get() = if (passwordPristine || password.isStrongPassword()) null else R.string.password_input_error

    val passwordRepeatError: (@StringRes Int)?
        get() = if (passwordRepeatPristine || passwordRepeat == password) null else R.string.password_repeat_not_matching

    val displayNameError: (@StringRes Int)?
        get() = if (displayNamePristine || displayName.isNotEmpty()) null else R.string.display_name_input_error
}
