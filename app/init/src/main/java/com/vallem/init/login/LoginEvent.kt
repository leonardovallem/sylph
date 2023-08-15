package com.vallem.init.login

sealed interface LoginEvent {
    sealed interface Update : LoginEvent {
        data class Name(val value: String) : Update
        data class Email(val value: String) : Update
        data class Password(val value: String) : Update
    }

    object SwitchMode : Update
    object SignIn : LoginEvent
    object SignUp : LoginEvent
    object SignOut : LoginEvent
}