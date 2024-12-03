package com.challenge.colour_spotted.spotted.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.colour_spotted.spotted.presentation.model.SpotterActionUiState
import com.challenge.colour_spotted.spotted.presentation.model.SpotterResultUiState
import com.challenge.colour_spotter.domain.usecase.GetColourInformationByHex
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class SpotterViewModel @Inject constructor(
    private val getColourInformationByHex : GetColourInformationByHex
) : ViewModel() {

    private val _resultUiState : MutableStateFlow<SpotterResultUiState> = MutableStateFlow(SpotterResultUiState.Idle)
    internal val resultUiState: StateFlow<SpotterResultUiState> by lazy { _resultUiState.asStateFlow() }

    private val _actionUIResult: MutableStateFlow<SpotterActionUiState> = MutableStateFlow(
        SpotterActionUiState.Action(
            text = "",
            onUpdate = { newText -> updateText(newText) },
            onClickAction = {
                performAction()
            }

        )
    )
    val actionUIResult: StateFlow<SpotterActionUiState> = _actionUIResult.asStateFlow()

    private suspend fun fetchColorDescription(hexColor : String) {
        _resultUiState.value = SpotterResultUiState.Loading
        withContext(Dispatchers.IO) {
            getColourInformationByHex(hexColor).onSuccess {
                _resultUiState.value = SpotterResultUiState.Result(it)
            }.onFailure {
                _resultUiState.value = SpotterResultUiState.Error(it.localizedMessage)
            }
        }
    }

    fun updateText(newText: String) {
        val actionState = _actionUIResult.value
        if ( actionState is SpotterActionUiState.Action) {
            _actionUIResult.value = actionState.copy (text = newText)
        }
    }

    fun performAction() {
        viewModelScope.launch {
            val actionState = _actionUIResult.value
            if ( actionState is SpotterActionUiState.Action) {
                fetchColorDescription(actionState.text)
            }
        }
    }

}

