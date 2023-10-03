package com.vallem.init.login

sealed interface LoginEvent {
    sealed interface Update : LoginEvent {
        data class Email(val value: String) : Update
        data class Password(val value: String) : Update
    }

    object SignIn : LoginEvent
}

data class LoginFormState(
    val email: String,
    val validEmail: Boolean,
    val password: String,
    val validPassword: Boolean,
    val validInput: Boolean,
)
