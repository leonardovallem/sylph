package com.vallem.sylph.events.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.vallem.sylph.shared.domain.model.Result
import com.vallem.sylph.shared.domain.model.event.Event
import com.vallem.sylph.shared.domain.model.event.EventDetails
import com.vallem.sylph.shared.domain.model.event.EventVote
import com.vallem.sylph.shared.domain.repository.EventUserVotesRepository
import com.vallem.sylph.shared.domain.repository.EventsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class EventDetailsViewModel @Inject constructor(
    private val eventsRepository: EventsRepository,
    private val votesRepository: EventUserVotesRepository,
    private val auth: FirebaseAuth,
) : ViewModel() {
    private val _eventResult = MutableStateFlow<Result<EventDetails?>>(Result.Loading)
    val eventResult = _eventResult.asStateFlow()

    private val _voteResult = MutableStateFlow<Result<EventVote?>>(Result.Loading)
    val voteResult = _voteResult.asStateFlow()

    private var eventJob: Job? = null
    private var voteJob: Job? = null

    val currentUserId: String?
        get() = auth.currentUser?.uid

    fun retrieveEventDetails(id: String) {
        eventJob?.cancel()
        eventJob = viewModelScope.launch {
            _eventResult.value = Result.Loading
            _voteResult.value = Result.Loading

            val eventDetails = async(Dispatchers.IO) {
                eventsRepository.retrieveEventDetails(id)
            }
            val vote = async(Dispatchers.IO) {
                when (val user = auth.currentUser) {
                    null -> Result.Failure(IllegalArgumentException("User was null"))
                    else -> votesRepository.retrieveUserVoteForEvent(user.uid, id)
                }
            }

            _eventResult.value = eventDetails.await()
            _voteResult.value = vote.await()
        }
    }

    fun vote(event: Event, vote: EventVote) {
        if (event.id == null || auth.currentUser == null) return

        voteJob?.cancel()
        voteJob = viewModelScope.launch {
            val currentVote = voteResult.value.getOrNull()
            _voteResult.value = Result.Loading

            withContext(Dispatchers.IO) {
                if (currentVote == vote) votesRepository.clearVote(
                    event.id!!,
                    auth.currentUser!!.uid
                ).onSuccess {
                    _voteResult.value = Result.Success(null)
                    updateVoteCount(event.id!!)
                }
                else votesRepository.vote(event.id!!, auth.currentUser!!.uid, vote).onSuccess {
                    _voteResult.value = Result.Success(vote)
                    updateVoteCount(event.id!!)
                }
            }.onFailure {
                _voteResult.value = Result.Failure(it)
            }
        }
    }

    private fun updateVoteCount(eventId: String) {
        voteJob?.cancel()
        voteJob = viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val updatedVoteCount = votesRepository.retrieveVoteCountsForEvent(eventId)
                    .getOrNull()
                    ?: return@withContext

                _eventResult.value.onSuccess {
                    _eventResult.update {
                        (it as Result.Success).copy(
                            data = it.data?.copy(voteCount = updatedVoteCount)
                        )
                    }
                }
            }
        }
    }
}