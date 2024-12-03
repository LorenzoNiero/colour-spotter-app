package com.challenge.colour_spotter.domain.repository

import com.challenge.colour_spotter.common.domain.model.ColorModel
import com.challenge.colour_spotter.domain.mapper.mapToDomainModel
import com.challenge.colour_spotter.network.data.NetworkDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class ColorRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource,
) : ColorRepository {

    private val regexHexColor = Regex("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")

    override suspend fun fetchColorInfo(hexColor : String) : ColorModel = withContext(Dispatchers.IO) {
        if (!isValidColorHex(hexColor)) {
            throw IllegalArgumentException("Invalid hex color code")
        }
        networkDataSource.fetchColorInfo(hex = hexColor).mapToDomainModel()
    }

     private fun isValidColorHex(hexColor: String): Boolean {
        return hexColor.matches( regexHexColor )
    }

}
