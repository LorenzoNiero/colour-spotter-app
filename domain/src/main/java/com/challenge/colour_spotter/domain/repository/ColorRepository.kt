package com.challenge.colour_spotter.domain.repository

import com.challenge.colour_spotter.common.domain.model.ColorModel

interface ColorRepository {

    suspend fun fetchColorInfo(hexColor: String): ColorModel
}
