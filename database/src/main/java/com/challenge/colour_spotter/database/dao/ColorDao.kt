package com.challenge.colour_spotter.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.challenge.colour_spotter.database.dao.base.IBaseDao
import com.challenge.colour_spotter.database.model.ColorEntity
import kotlinx.coroutines.flow.Flow


@Dao
abstract class ColorDao : IBaseDao<ColorEntity>  {

    @Transaction
    @Query(value = "SELECT * FROM color ORDER BY name DESC")
    abstract fun observeAllColor(): Flow<List<ColorEntity>>

    @Query(value = "DELETE FROM color WHERE id = :id")
    abstract suspend fun deleteById(id: String)

}