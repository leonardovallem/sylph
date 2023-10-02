package com.vallem.init.login

import android.util.Patterns
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.vallem.componentlibrary.util.ValidationRule
import com.vallem.sylph.shared.domain.model.Result
import com.vallem.sylph.shared.domain.repository.AuthRepository
import com.vallem.sylph.shared.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {
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

    val validName by derivedStateOf { !hasNameFirstInput || name.isNotBlank() }
    val validEmail by derivedStateOf {
        !hasEmailFirstInput || Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    val validPassword by derivedStateOf {
        !hasPasswordFirstInput
                || (!isRegister && password.isNotEmpty())
                || ValidationRule.Password.isValid(password)
    }

    val validInput by derivedStateOf {
        if (isRegister) {
            if (!hasNameFirstInput || !hasEmailFirstInput || !hasPasswordFirstInput) false
            else validEmail && validPassword && validName
        } else {
            if (!hasEmailFirstInput || !hasPasswordFirstInput) false
            else validEmail && validPassword
        }
    }

    var loginResult by mutableStateOf<Result<FirebaseUser>?>(null)
    var signUpResult by mutableStateOf<Result<FirebaseUser>?>(null)

    fun onEvent(event: LoginEvent): Any = when (event) {
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

        is LoginEvent.SwitchMode -> {
            if (!isRegister) hasNameFirstInput = true
            isRegister = !isRegister
        }

        is LoginEvent.SignIn -> viewModelScope.launch {
            loginResult = Result.Loading
            loginResult = authRepository.login(email, password)
        }

        is LoginEvent.SignUp -> viewModelScope.launch {
            signUpResult = Result.Loading
            signUpResult = authRepository.signup(name, email, password)

            when (val res = signUpResult) {
                is Result.Success -> userRepository.save(
                    id = res.data.uid,
                    name = res.data.displayName.orEmpty(),
                    picUrl = null
                )

                else -> Unit
            }
        }

        is LoginEvent.SignOut -> viewModelScope.launch {
            authRepository.logout()
            loginResult = null
            signUpResult = null
        }
    }
}