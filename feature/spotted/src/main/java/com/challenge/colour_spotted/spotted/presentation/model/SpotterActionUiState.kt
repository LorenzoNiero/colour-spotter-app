package com.challenge.colour_spotted.spotted.presentation.model

sealed class SpotterActionUiState {
    data class Action(
        val text : String,
        val onUpdate: (newText : String) -> Unit,
        val onClickAction: () -> Unit,
    ) : SpotterActionUiState()
}