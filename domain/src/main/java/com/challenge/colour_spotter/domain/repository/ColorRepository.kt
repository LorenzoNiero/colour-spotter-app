package com.challenge.colour_spotter.domain.repository

import com.challenge.colour_spotter.common.domain.model.ColorModel
import kotlinx.coroutines.flow.Flow

interface ColorRepository {

    suspend fun fetchColorInfo(hexColor: String): ColorModel
    fun observeAllColor(desc : Boolean): Flow<List<ColorModel>>
    suspend fun deleteColor(color: ColorModel)
}
