package com.challenge.colour_spotter.camera.extension


fun Int.toHexColor() : String{
    return String.format("#%06X", 0xFFFFFF and this)
}