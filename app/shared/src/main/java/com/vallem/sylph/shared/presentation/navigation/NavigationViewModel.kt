package com.vallem.sylph.shared.presentation.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.vallem.componentlibrary.domain.model.UserInfo
import com.vallem.sylph.shared.domain.model.Result
import com.vallem.sylph.shared.domain.repository.AuthRepository
import com.vallem.sylph.shared.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class NavigationViewModel @Inject constructor(
    auth: FirebaseAuth,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _userInfo = MutableStateFlow<Result<UserInfo?>>(Result.Loading)
    val userInfo = _userInfo.asStateFlow()

    private val _navigationEvent = Channel<NavigationEvent?>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    init {
        auth.currentUser?.let {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    _userInfo.value = userRepository.retrieveUserInfo(it.uid)
                }
            }
        }
    }

    fun logOut() = viewModelScope.launch {
        authRepository.logout()
        _navigationEvent.send(NavigationEvent.LogOut)
    }
}

enum class NavigationEvent {
    LogOut
}