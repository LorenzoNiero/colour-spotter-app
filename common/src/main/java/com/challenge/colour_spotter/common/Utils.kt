package com.challenge.colour_spotter.common

fun String.addHashIfNeeded(): String {
    return if (this.startsWith("#")) {
        this
    } else {
        "#$this"
    }
}