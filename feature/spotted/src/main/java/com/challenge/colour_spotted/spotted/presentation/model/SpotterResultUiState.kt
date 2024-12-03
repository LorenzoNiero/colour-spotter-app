package com.challenge.colour_spotted.spotted.presentation.model

import com.challenge.colour_spotter.common.domain.model.ColorModel

sealed class SpotterResultUiState {
    data object Loading : SpotterResultUiState()

    data class Error(
        val message: String?
    ) : SpotterResultUiState()

    data object Idle : SpotterResultUiState()

    data class Result(val color : ColorModel) : SpotterResultUiState()

}


//sealed class SpotterResultUiState {
//    data object Loading : SpotterResultUiState()
//
//    data class Error(
//        val message: String?
//    ) : SpotterResultUiState()
//
//    data object Idle : SpotterResultUiState()
//
//    data class Result(val color : ColorModel) : SpotterResultUiState()
//
//}