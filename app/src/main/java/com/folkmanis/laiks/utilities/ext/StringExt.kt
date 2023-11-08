package com.folkmanis.laiks.utilities.ext

import android.util.Patterns
import java.util.regex.Pattern

private const val MIN_PASS_LENGTH = 6
private const val STRONG_PASS_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$"
private const val PASS_PATTERN = "^\\S+\$"

fun String.isValidEmail(): Boolean =
    this.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches()


fun String.isStrongPassword(): Boolean =
    this.isNotBlank() &&
            this.length >= MIN_PASS_LENGTH &&
            Pattern.compile(STRONG_PASS_PATTERN).matcher(this).matches()


fun String.isValidPassword(): Boolean =
    this.isNotBlank() &&
            Pattern.compile(PASS_PATTERN).matcher(this).matches()

