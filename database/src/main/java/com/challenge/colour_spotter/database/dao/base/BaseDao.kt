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


abstract class BaseDao<T : BaseEntity>(val tableName: String): IBaseDao<T> {

    @RawQuery
    protected abstract suspend fun getEntityRaw(query: SupportSQLiteQuery): List<T>?

    suspend fun getEntity(id: String): T? {
        return getEntity(listOf(id))?.firstOrNull()
    }

    suspend fun getEntity(ids: List<String>): List<T>? {
        val result = StringBuilder()
        for (index in ids.indices) {
            if (index != 0) {
                result.append(",")
            }
            result.append("'").append(ids[index]).append("'")
        }
        val query = SimpleSQLiteQuery("SELECT * FROM $tableName WHERE id IN ($result);")
        return getEntityRaw(query)
    }

    @RawQuery
    protected abstract suspend fun getAll(query: SupportSQLiteQuery): List<T>?

    suspend fun getAll(): List<T>? {
        val query = SimpleSQLiteQuery("SELECT * FROM $tableName ;")
        return getAll(query)
    }

    /**
     * Deletes rows in the db matching the specified [ids]
     */
    @RawQuery
    protected abstract suspend fun deleteRaw(query: SupportSQLiteQuery): List<T>?

    suspend fun delete(id: String): T? {
        return delete(listOf(id))?.firstOrNull()
    }

    suspend fun delete(ids: List<String>): List<T>? {
        val result = StringBuilder()
        for (index in ids.indices) {
            if (index != 0) {
                result.append(",")
            }
            result.append("'").append(ids[index]).append("'")
        }
        val query = SimpleSQLiteQuery("DELETE FROM $tableName WHERE id IN ($result);")
        return deleteRaw(query)
    }
}

