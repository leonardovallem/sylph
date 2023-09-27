package com.vallem.sylph.events.presentation.user.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vallem.sylph.shared.domain.model.Result
import com.vallem.sylph.shared.domain.model.UserDetails
import com.vallem.sylph.shared.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    private val _result = MutableStateFlow<Result<UserDetails?>>(Result.Loading)
    val result = _result.asStateFlow()

    private var job: Job? = null

    fun retrieveUserDetails(userId: String) {
        _result.value = Result.Loading
        job?.cancel()

        job = viewModelScope.launch {
            _result.value = repository.retrieveDetails(userId)
        }
    }
}