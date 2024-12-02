package com.challenge.colour_spotter.network.data

import com.challenge.colour_spotter.network.data.model.ColorResponse


/**
 * Interface representing network calls to the ColorApi backend
 */
interface NetworkDataSource {
    suspend fun fetchColorInfo(hex: String): ColorResponse
}