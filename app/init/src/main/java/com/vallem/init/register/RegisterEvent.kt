package com.vallem.init.register

import android.graphics.Bitmap

sealed interface RegisterEvent {
    sealed interface Update : RegisterEvent {
        data class Name(val value: String) : Update
        data class Email(val value: String) : Update
        data class Password(val value: String) : Update
        data class PasswordConfirmation(val value: String) : Update
        data class Picture(val value: Bitmap) : Update
    }

    object SignUp : RegisterEvent
}

data class RegisterFormState(
    val name: String,
    val validName: Boolean,
    val email: String,
    val validEmail: Boolean,
    val password: String,
    val validPassword: Boolean,
    val passwordConfirmation: String,
    val validPasswordConfirmation: Boolean,
    val validInput: Boolean,
)
