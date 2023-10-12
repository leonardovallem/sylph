package com.vallem.init.register

import android.graphics.Bitmap
import android.util.Patterns
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.vallem.componentlibrary.extensions.encodeAsBase64
import com.vallem.componentlibrary.util.ValidationRule
import com.vallem.sylph.shared.auth.AuthData
import com.vallem.sylph.shared.domain.model.Result
import com.vallem.sylph.shared.domain.repository.FirebaseAuthRepository
import com.vallem.sylph.shared.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: FirebaseAuthRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    var signUpResult by mutableStateOf<Result<FirebaseUser>?>(null)
        private set

    var name by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var passwordConfirmation by mutableStateOf("")
        private set
    private var picture by mutableStateOf<Bitmap?>(null)

    private var hasNameFirstInput by mutableStateOf(false)
    private var hasEmailFirstInput by mutableStateOf(false)
    private var hasPasswordFirstInput by mutableStateOf(false)

    val validName by derivedStateOf { !hasNameFirstInput || name.isNotBlank() }
    val validEmail by derivedStateOf {
        !hasEmailFirstInput || Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    val validPassword by derivedStateOf {
        !hasPasswordFirstInput || ValidationRule.Password.isValid(password)
    }

    val validPasswordConfirmation by derivedStateOf {
        !validPassword || password == passwordConfirmation
    }

    val validInput by derivedStateOf {
        if (!hasNameFirstInput || !hasEmailFirstInput || !hasPasswordFirstInput) false
        else validEmail && validPassword && validName && password == passwordConfirmation
    }

    fun onEvent(event: RegisterEvent): Any = when (event) {
        is RegisterEvent.Update.Email -> {
            hasEmailFirstInput = true
            email = event.value
        }

        is RegisterEvent.Update.Name -> {
            hasNameFirstInput = true
            name = event.value
        }

        is RegisterEvent.Update.Password -> {
            hasPasswordFirstInput = true
            password = event.value
        }

        is RegisterEvent.Update.PasswordConfirmation -> passwordConfirmation = event.value
        is RegisterEvent.Update.Picture -> picture = event.value

        RegisterEvent.SignUp -> signUp()
    }

    private fun signUp() = viewModelScope.launch {
        signUpResult = Result.Loading
        withContext(Dispatchers.IO) {
            signUpResult = authRepository.auth(AuthData.Email.Register(name, email, password))

            when (val res = signUpResult) {
                is Result.Success -> userRepository.save(
                    id = res.data.uid,
                    name = res.data.displayName.orEmpty(),
                    picture = picture?.encodeAsBase64()
                )

                else -> Unit
            }
        }
    }
}
