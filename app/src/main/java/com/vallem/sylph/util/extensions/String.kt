package com.vallem.sylph.util.extensions

operator fun String.times(count: Int): String {
    var str = ""
    for (i in 0 until count) str += this
    return str
}