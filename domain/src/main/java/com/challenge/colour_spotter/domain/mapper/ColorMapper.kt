package com.challenge.colour_spotter.domain.mapper

import com.challenge.colour_spotter.common.domain.model.ColorModel
import com.challenge.colour_spotter.network.data.model.ColorResponse

internal fun ColorResponse.mapToDomainModel() : ColorModel = ColorModel(
    name = this.name.value,
    hex = this.hex.value
)