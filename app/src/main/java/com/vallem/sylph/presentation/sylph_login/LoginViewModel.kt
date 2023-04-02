package com.vallem.sylph.presentation.sylph_login

import android.util.Patterns
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.vallem.componentlibrary.util.ValidationRule
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    var isRegister by mutableStateOf(false)
        private set

    var name by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set

    private var hasNameFirstInput by mutableStateOf(false)
    private var hasEmailFirstInput by mutableStateOf(false)
    private var hasPasswordFirstInput by mutableStateOf(false)

    val validName by derivedStateOf {
        !hasNameFirstInput || name.isNotBlank()
    }
    val validEmail by derivedStateOf {
        !hasEmailFirstInput || Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    val validPassword by derivedStateOf {
        !hasPasswordFirstInput || ValidationRule.Password.isValid(password)
    }

    fun onEvent(event: LoginEvent) = when (event) {
        is LoginEvent.Update.Name -> {
            hasNameFirstInput = true
            name = event.value
        }

        is LoginEvent.Update.Email -> {
            hasEmailFirstInput = true
            email = event.value
        }

        is LoginEvent.Update.Password -> {
            hasPasswordFirstInput = true
            password = event.value
        }

        is LoginEvent.SwitchMode -> isRegister = !isRegister

        LoginEvent.Login -> Unit
    }
}