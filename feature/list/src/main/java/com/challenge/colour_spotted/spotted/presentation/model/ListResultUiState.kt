package com.challenge.colour_spotted.spotted.presentation.model

import com.challenge.colour_spotter.common.domain.model.ColorModel

sealed class ListResultUiState {
    data object Loading : ListResultUiState()

    data class Error(
        val message: String?,
    ) : ListResultUiState()

    data class List(val list : ColorModel) : ListResultUiState()
}


