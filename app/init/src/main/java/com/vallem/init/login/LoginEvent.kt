package com.vallem.init.login

import com.google.firebase.auth.AuthCredential

sealed interface LoginEvent {
    sealed interface Update : LoginEvent {
        data class Email(val value: String) : Update
        data class Password(val value: String) : Update
    }

    sealed interface SignIn : LoginEvent {
        object WithCurrentData : SignIn
        data class WithGoogle(val credential: AuthCredential) : SignIn
    }
}

data class LoginFormState(
    val email: String,
    val validEmail: Boolean,
    val password: String,
    val validPassword: Boolean,
    val validInput: Boolean,
    val hasGoogleFunctionality: Boolean,
)
