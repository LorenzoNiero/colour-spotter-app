package com.challenge.colour_spotted.list.presentation.model

import com.challenge.colour_spotter.common.domain.model.ColorModel

sealed class ListUiState {
    data object Loading : ListUiState()
    data object Empty : ListUiState()

    data class Error(
        val message: String?,
    ) : ListUiState()

    data class Result(
        val colors : List<ColorModel>
    ) : ListUiState()
}


