package com.challenge.colour_spotted.spotted.presentation.model

sealed class SpotterUiState {
    data object Loading : SpotterUiState()

    data class Error(
        val message: String?
    ) : SpotterUiState()

    data class Result(val message: String) : SpotterUiState()

}