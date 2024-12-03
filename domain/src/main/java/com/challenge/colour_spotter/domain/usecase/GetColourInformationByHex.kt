package com.challenge.colour_spotter.domain.usecase

import com.challenge.colour_spotter.common.domain.model.ColorModel
import com.challenge.colour_spotter.domain.repository.ColorRepository
import javax.inject.Inject

class GetColourInformationByHex @Inject constructor(
    private val repository: ColorRepository
) {

    suspend operator fun invoke( hexColor : String): Result<ColorModel> =
        runCatching { repository.fetchColorInfo(hexColor) }

}