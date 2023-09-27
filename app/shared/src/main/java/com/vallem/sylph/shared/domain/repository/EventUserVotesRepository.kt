package com.vallem.sylph.shared.domain.repository

import com.vallem.sylph.shared.domain.model.event.EventVote
import com.vallem.sylph.shared.domain.model.event.VoteCount
import com.vallem.sylph.shared.domain.model.Result

interface EventUserVotesRepository {
    suspend fun vote(eventId: String, votingUserId: String, vote: EventVote): Result<Unit>

    // TODO add vote statistics to [EventDetailsBottomSheet]
    suspend fun retrieveVoteCountsForEvent(eventId: String): Result<VoteCount>

    suspend fun retrieveVoteCountsForUserEvents(userId: String): Result<VoteCount>
}
