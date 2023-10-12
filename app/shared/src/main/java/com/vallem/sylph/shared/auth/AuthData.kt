package com.vallem.sylph.shared.auth

import com.google.firebase.auth.AuthCredential

sealed interface AuthData {
    sealed class Email(
        open val email: String,
        open val password: String,
    ) : AuthData {
        data class Login(
            override val email: String,
            override val password: String,
        ) : Email(email, password)

        data class Register(
            val name: String,
            override val email: String,
            override val password: String,
        ) : Email(email, password)
    }

    sealed class Google(open val credential: AuthCredential) : AuthData {
        data class OneTapSignIn(override val credential: AuthCredential) : Google(credential)
    }
}
