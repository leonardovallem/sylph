package com.vallem.sylph.shared.domain.model.event

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VoteCount(val upVotes: Int, val downVotes: Int) : Parcelable