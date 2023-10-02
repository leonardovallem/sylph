package com.vallem.sylph.shared.data.dynamo.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val name: String,
    val picture: String?,
    val userId: String,
) : Parcelable
