package com.vallem.init.login

import android.util.Patterns
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.vallem.sylph.shared.auth.AuthData
import com.vallem.sylph.shared.auth.GoogleSignInClient
import com.vallem.sylph.shared.domain.model.Result
import com.vallem.sylph.shared.domain.repository.FirebaseAuthRepository
import com.vallem.sylph.shared.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Named
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: FirebaseAuthRepository,
    private val userRepository: UserRepository,
    val googleSignInClient: GoogleSignInClient,
    @Named("HasGoogleFunctionality") val hasGoogleFunctionality: Boolean,
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
        private set

    fun onEvent(event: LoginEvent): Any = when (event) {
        is LoginEvent.Update.Email -> {
            hasEmailFirstInput = true
            email = event.value
        }

        is LoginEvent.Update.Password -> {
            hasPasswordFirstInput = true
            password = event.value
        }

        is LoginEvent.SignIn.WithGoogle -> viewModelScope.launch {
            loginResult = Result.Loading
            withContext(Dispatchers.IO) {
                loginResult = authRepository.auth(AuthData.Google.OneTapSignIn(event.credential))

                loginResult?.getOrNull()?.let {
                    userRepository.save(
                        id = it.uid,
                        name = it.displayName.orEmpty(),
                        picture = null,
                    )
                }
            }
        }

        LoginEvent.SignIn.WithCurrentData -> viewModelScope.launch {
            loginResult = Result.Loading
            withContext(Dispatchers.IO) {
                loginResult = authRepository.auth(AuthData.Email.Login(email, password))

                loginResult?.getOrNull()?.let {
                    userRepository.save(
                        id = it.uid,
                        name = it.displayName.orEmpty(),
                        picture = null,
                    )
                }
            }
        }
    }
}