package com.challenge.colour_spotter.domain.mapper

import com.challenge.colour_spotter.common.domain.model.ColorModel
import com.challenge.colour_spotter.database.model.ColorEntity
import com.challenge.colour_spotter.network.data.model.ColorResponse

internal fun ColorResponse.mapToDomainModel() : ColorModel = ColorModel(
    id = hex.clean ,
    name = this.name.value,
    hex = this.hex.value
)

internal fun ColorResponse.toEntityModel(): ColorEntity = ColorEntity(
    id = hex.clean,
    hex = hex.value,
    name = name.value
)

fun ColorEntity.asExternalModel() : ColorModel = ColorModel(
    id = id,
    name = name,
    hex = hex
)