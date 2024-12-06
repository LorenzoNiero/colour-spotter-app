package com.challenge.colour_spotter.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.challenge.colour_spotter.database.dao.ColorDao
import com.challenge.colour_spotter.database.model.ColorEntity


const val VERSION_DATABASE = 1

@Database(
    entities = [
        ColorEntity::class,
    ],
    version = VERSION_DATABASE,
    autoMigrations = [
//         AutoMigration (from = 1, to = VERSION_DATABASE),
                ],
    exportSchema = true,
)

@TypeConverters(
    //todo: add converters
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun colorDao(): ColorDao
}





