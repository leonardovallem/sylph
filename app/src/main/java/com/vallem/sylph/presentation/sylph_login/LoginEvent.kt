package com.vallem.sylph.presentation.sylph_login

sealed interface LoginEvent {
    sealed interface Update : LoginEvent {
        data class Name(val value: String) : Update
        data class Email(val value: String) : Update
        data class Password(val value: String) : Update
    }

    object SwitchMode : Update
    object Login : LoginEvent
}