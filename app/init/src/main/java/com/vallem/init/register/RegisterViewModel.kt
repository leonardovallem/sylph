package com.vallem.init.register

import android.util.Patterns
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.vallem.componentlibrary.util.ValidationRule
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class RegisterViewModel @Inject constructor() : ViewModel() {
    private val _picUrl = MutableStateFlow<String?>(null)
    val picUrl = _picUrl.asStateFlow()

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
        !hasPasswordFirstInput || ValidationRule.Password.isValid(password)
    }

    val validInput by derivedStateOf {
        if (!hasNameFirstInput || !hasEmailFirstInput || !hasPasswordFirstInput) false
        else validEmail && validPassword && validName
    }

    fun setPicUrl(url: String) = _picUrl.update { url }

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
}
