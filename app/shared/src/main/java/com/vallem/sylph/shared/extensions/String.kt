package com.vallem.sylph.shared.extensions

operator fun String.times(count: Int): String {
    var str = ""
    for (i in 0 until count) str += this
    return str
}

fun String.truncate(size: Int) = if (length < size) this else take(size).plus("...")
