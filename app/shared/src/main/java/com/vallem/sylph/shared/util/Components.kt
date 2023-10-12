package com.vallem.sylph.shared.util

import android.content.Context
import android.widget.Toast

fun Context.showToastMessage(text: String, duration: Int = Toast.LENGTH_SHORT) = Toast
    .makeText(this, text, duration)
    .show()
