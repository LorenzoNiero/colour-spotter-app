package com.challenge.colour_spotted.spotted.presentation.model

sealed class SpotterActionUiState {
    data class Action(
        val textField : TextFieldAction?,
        val onStartOrStopClickAction: () -> Unit
    ) : SpotterActionUiState()
}

data class TextFieldAction(
    val text : String,
    val onUpdate: (newText : String) -> Unit,
    val onClickAction: () -> Unit,
)