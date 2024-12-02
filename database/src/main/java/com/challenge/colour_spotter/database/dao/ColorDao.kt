package com.challenge.colour_spotter.database.dao

import androidx.room.Dao
import com.challenge.colour_spotter.database.dao.base.BaseDao
import com.challenge.colour_spotter.database.model.ColorEntity


@Dao
abstract class ColorDao : BaseDao<ColorEntity>(ColorEntity.TABLE_NAME)  {

}