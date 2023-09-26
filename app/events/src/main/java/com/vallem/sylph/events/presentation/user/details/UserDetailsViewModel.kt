package com.vallem.sylph.events.presentation.user.details

import androidx.lifecycle.ViewModel
import com.vallem.sylph.events.domain.UserDetails
import com.vallem.sylph.shared.domain.model.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class UserDetailsViewModel @Inject constructor() : ViewModel() {
    private val _result = MutableStateFlow<Result<UserDetails?>>(Result.Loading)
    val result = _result.asStateFlow()
}