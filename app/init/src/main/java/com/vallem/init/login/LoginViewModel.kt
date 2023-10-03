package com.vallem.init.login

import android.util.Patterns
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.vallem.sylph.shared.domain.model.Result
import com.vallem.sylph.shared.domain.repository.AuthRepository
import com.vallem.sylph.shared.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set

    private var hasEmailFirstInput by mutableStateOf(false)
    private var hasPasswordFirstInput by mutableStateOf(false)

    val validEmail by derivedStateOf {
        !hasEmailFirstInput || Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    val validPassword by derivedStateOf {
        !hasPasswordFirstInput || password.isNotEmpty()
    }

    val validInput by derivedStateOf {
        if (!hasEmailFirstInput || !hasPasswordFirstInput) false
        else validEmail && validPassword
    }

    var loginResult by mutableStateOf<Result<FirebaseUser>?>(null)

    fun onEvent(event: LoginEvent): Any = when (event) {
        is LoginEvent.Update.Email -> {
            hasEmailFirstInput = true
            email = event.value
        }

        is LoginEvent.Update.Password -> {
            hasPasswordFirstInput = true
            password = event.value
        }

        is LoginEvent.SignIn -> viewModelScope.launch {
            loginResult = Result.Loading
            withContext(Dispatchers.IO) {
                loginResult = authRepository.login(email, password)

                when (val res = loginResult) {
                    is Result.Success -> userRepository.save(
                        id = res.data.uid,
                        name = res.data.displayName.orEmpty(),
                        picture = null
                    )

                    else -> Unit
                }
            }
        }
    }
}