package com.vallem.componentlibrary.util

fun Array<out String>.pluralize(count: Int) =
    if (count > 1) joinToString(" ") { it + "s" } else joinToString(" ")

fun String.pluralize(count: Int) = if (count > 1) plus("s") else this