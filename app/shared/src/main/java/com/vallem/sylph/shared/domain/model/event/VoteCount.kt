package com.vallem.sylph.shared.domain.model.event

import android.os.Parcelable
import kotlin.math.max
import kotlinx.parcelize.Parcelize

@Parcelize
data class VoteCount(val upVotes: Int, val downVotes: Int) : Parcelable {
    operator fun minus(vote: EventVote) = when (vote) {
        EventVote.UpVote -> copy(upVotes = max(upVotes - 1, 0))
        EventVote.DownVote -> copy(downVotes = max(downVotes - 1, 0))
    }

    operator fun plus(vote: EventVote) = when (vote) {
        EventVote.UpVote -> copy(upVotes = upVotes + 1)
        EventVote.DownVote -> copy(downVotes = downVotes + 1)
    }
}