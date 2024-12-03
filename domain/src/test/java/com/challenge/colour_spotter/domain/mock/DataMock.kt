package com.challenge.colour_spotter.domain.mock

import com.challenge.colour_spotter.network.data.model.ColorResponse
import com.challenge.colour_spotter.network.data.model.Contrast
import com.challenge.colour_spotter.network.data.model.Hex
import com.challenge.colour_spotter.network.data.model.Name


object DataMock {

    val colorNetwork = ColorResponse(
        hex = Hex(value = "#24B1E0", clean = "24B1E0"),
        name = Name(
            value = "Cerulean",
            closestNamedHex = "#1DACD6",
            exactMatchName = false,
            distance = 356
        ),
        contrast = Contrast(value = "#000000"),
    )
}