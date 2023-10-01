package com.vallem.sylph.shared.domain.repository

import com.vallem.sylph.shared.domain.model.Result
import com.vallem.sylph.shared.domain.model.event.EventVote
import com.vallem.sylph.shared.domain.model.event.VoteCount

interface EventUserVotesRepository {
    suspend fun vote(eventId: String, votingUserId: String, vote: EventVote): Result<Unit>
    suspend fun clearVote(eventId: String, votingUserId: String): Result<Unit>

    suspend fun retrieveUserVoteForEvent(userId: String, eventId: String): Result<EventVote?>

    // TODO add vote statistics to [EventDetailsBottomSheet]
    suspend fun retrieveVoteCountsForEvent(eventId: String): Result<VoteCount>

    suspend fun retrieveVoteCountsForUserEvents(userId: String): Result<VoteCount>
}
