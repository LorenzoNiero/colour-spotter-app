package com.challenge.colour_spotter.database.dao.base

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.challenge.colour_spotter.database.model.BaseEntity
import com.challenge.colour_spotter.database.model.IEntity


interface IBaseDao<T : IEntity> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entities: List<T>)

    @Update
    fun update(entity: T)

    @Update
    fun update(entities: List<T>)

    @Update
    suspend fun updateAsync(entity: T)

    @Update
    suspend fun updateAsync(entities: List<T>)

    @Delete
    fun delete(entity: T)

    @Delete
    fun delete(entities: List<T>)
}
