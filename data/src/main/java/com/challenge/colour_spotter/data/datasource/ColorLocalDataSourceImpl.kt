package com.challenge.colour_spotter.data.datasource

import com.challenge.colour_spotter.database.dao.ColorDao
import com.challenge.colour_spotter.database.model.ColorEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class ColorLocalDataSourceImpl @Inject constructor(
    private val colorDao: ColorDao,
): ColorLocalDataSource {

    override fun onColorsUpdate(desc : Boolean): Flow<List<ColorEntity>> = colorDao.observeAllColor()

    override fun insertColor(color: ColorEntity) {
        colorDao.insert(color)
    }

    override suspend fun deleteColor(colorId: String) {
        colorDao.deleteById(colorId)
    }

}
