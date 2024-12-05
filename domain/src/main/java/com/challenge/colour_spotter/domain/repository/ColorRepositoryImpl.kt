package com.challenge.colour_spotter.domain.repository

import com.challenge.colour_spotter.common.domain.model.ColorModel
import com.challenge.colour_spotter.data.datasource.ColorLocalDataSource
import com.challenge.colour_spotter.domain.mapper.asExternalModel
import com.challenge.colour_spotter.domain.mapper.toEntityModel
import com.challenge.colour_spotter.network.data.NetworkDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class ColorRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val colorLocalDataSource: ColorLocalDataSource
) : ColorRepository {

    private val regexHexColor = Regex("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")

    override suspend fun fetchColorInfo(hexColor: String): ColorModel =
        withContext(Dispatchers.IO) {
            if (!isValidColorHex(hexColor)) {
                throw IllegalArgumentException("Invalid hex color code")
            }
            val colorNetwork = networkDataSource.fetchColorInfo(hex = hexColor)
            val colorEntity = colorNetwork.toEntityModel()
            colorLocalDataSource.insertColor(colorEntity)
            colorEntity.asExternalModel()
        }

    private fun isValidColorHex(hexColor: String): Boolean {
        return hexColor.matches(regexHexColor)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeAllColor(desc : Boolean): Flow<List<ColorModel>> =
        colorLocalDataSource.onColorsUpdate(desc).mapLatest { dog ->
            dog.map { it.asExternalModel() }
        }

    override suspend fun deleteColor(color: ColorModel) = colorLocalDataSource.deleteColor(color.id)

}

