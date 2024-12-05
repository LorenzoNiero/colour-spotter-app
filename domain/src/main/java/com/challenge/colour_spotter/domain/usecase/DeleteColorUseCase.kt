package com.challenge.colour_spotter.domain.usecase

import com.challenge.colour_spotter.common.domain.model.ColorModel
import com.challenge.colour_spotter.domain.repository.ColorRepository
import javax.inject.Inject

class DeleteColorUseCase @Inject constructor(
    private val colorRepository: ColorRepository
) {
    suspend operator fun invoke(color : ColorModel): Unit = colorRepository.deleteColor(color)
}