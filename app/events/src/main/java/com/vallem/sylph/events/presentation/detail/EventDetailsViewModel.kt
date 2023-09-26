package com.vallem.sylph.events.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vallem.sylph.shared.domain.model.Result
import com.vallem.sylph.shared.domain.model.event.Event
import com.vallem.sylph.shared.domain.repository.EventsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class EventDetailsViewModel @Inject constructor(
    private val repository: EventsRepository
) : ViewModel() {
    private val _result = MutableStateFlow<Result<Event?>>(Result.Loading)
    val result = _result.asStateFlow()

    private var job: Job? = null

    fun retrieveEventDetails(id: String) {
        job?.cancel()
        job = viewModelScope.launch {
            _result.value = Result.Loading
            _result.value = withContext(Dispatchers.IO) {
                repository.retrieveEventDetails(id)
            }
        }
    }
}