package com.challenge.colour_spotter.data.datasource

import com.challenge.colour_spotter.database.model.ColorEntity
import kotlinx.coroutines.flow.Flow

interface ColorLocalDataSource {

    fun onColorsUpdate(desc : Boolean): Flow<List<ColorEntity>>
    fun insertColor(color: ColorEntity)
    suspend fun deleteColor(colorId: String)

}
