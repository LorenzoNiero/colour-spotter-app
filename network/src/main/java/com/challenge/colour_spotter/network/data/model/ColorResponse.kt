package com.challenge.colour_spotter.network.data.model

import com.challenge.colour_spotter.network.data.model.base.NetworkResponseBase
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class ColorResponse (
    val hex: Hex,
    val contrast: Contrast,
    val name: Name,
) : NetworkResponseBase()

data class Contrast (
    val value: String
)

data class Hex (
    val value: String,
    val clean: String
)
data class Name (
    val value: String,
    @Json(name = "closest_named_hex")
    val closestNamedHex: String,
    @Json(name = "exact_match_name")
    val exactMatchName: Boolean,
    val distance: Long
)