package com.challenge.colour_spotter.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = ColorEntity.TABLE_NAME)
class ColorEntity (
    @PrimaryKey()
    override val id: String,
    val name: String,
    val hex: String
    ) : BaseEntity() {

    companion object {
        const val TABLE_NAME = "color"
    }
}