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
import com.vallem.sylph.shared.domain.model.Result
import com.vallem.sylph.shared.domain.repository.AuthRepository
import com.vallem.sylph.shared.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
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
    var picture by mutableStateOf<Bitmap?>(null)
        private set

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

    val validInput by derivedStateOf {
        if (!hasNameFirstInput || !hasEmailFirstInput || !hasPasswordFirstInput) false
        else validEmail && validPassword && validName
    }

    fun updateName(value: String) {
        hasNameFirstInput = true
        name = value
    }

    fun updateEmail(value: String) {
        hasEmailFirstInput = true
        email = value
    }

    fun updatePassword(value: String) {
        hasPasswordFirstInput = true
        password = value
    }

    fun updatePicture(bitmap: Bitmap) {
        picture = bitmap
    }

    fun signUp() = viewModelScope.launch {
        signUpResult = Result.Loading
        withContext(Dispatchers.IO) {
            signUpResult = authRepository.signup(name, email, password)

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
