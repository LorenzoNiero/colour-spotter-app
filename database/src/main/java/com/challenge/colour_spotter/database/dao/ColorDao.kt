package com.challenge.colour_spotter.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.challenge.colour_spotter.database.dao.base.IBaseDao
import com.challenge.colour_spotter.database.model.ColorEntity
import kotlinx.coroutines.flow.Flow


@Dao
abstract class ColorDao : IBaseDao<ColorEntity>  {

    @Transaction
    @Query(value = "SELECT * FROM color ORDER BY name DESC")
    abstract fun observeAllColor(): Flow<List<ColorEntity>>

    @Transaction
    @RawQuery(observedEntities = [ColorEntity::class])
    protected abstract fun colorByQuery(query: SupportSQLiteQuery): Flow<List<ColorEntity>>

    fun observeAllColorOrderBy(isDesc: Boolean): Flow<List<ColorEntity>> {
        val sortOrder = if (isDesc) "DESC" else "ASC"
        val query = SimpleSQLiteQuery("SELECT * FROM color ORDER BY name $sortOrder")
        return colorByQuery(query)
    }

    @Query(value = "DELETE FROM color WHERE id = :id")
    abstract suspend fun deleteById(id: String)

}