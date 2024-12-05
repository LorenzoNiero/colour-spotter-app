package com.challenge.colour_spotter.domain.usecase

import com.challenge.colour_spotter.common.domain.model.ColorModel
import com.challenge.colour_spotter.domain.repository.ColorRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveColorsUseCase @Inject constructor(
    private val colorRepository: ColorRepository
) {
    operator fun invoke(desc : Boolean): Flow<List<ColorModel>> = colorRepository.observeAllColor(desc)
}

