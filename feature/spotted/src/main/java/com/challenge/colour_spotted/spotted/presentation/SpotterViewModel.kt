package com.challenge.colour_spotted.spotted.presentation

import androidx.lifecycle.ViewModel
import com.challenge.colour_spotted.spotted.presentation.model.SpotterResultUiState
import com.challenge.colour_spotter.domain.usecase.GetColourInformationByHex
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class SpotterViewModel @Inject constructor(
    private val getColourInformationByHex : GetColourInformationByHex
) : ViewModel() {

    private val _uiState : MutableStateFlow<SpotterResultUiState> = MutableStateFlow(SpotterResultUiState.Idle)
    internal val uiState: StateFlow<SpotterResultUiState> by lazy { _uiState.asStateFlow() }

    private suspend fun fetchColorDescription(hexColor : String) {
        _uiState.value = SpotterResultUiState.Loading
        withContext(Dispatchers.IO) {
            getColourInformationByHex(hexColor).onSuccess {
                _uiState.value = SpotterResultUiState.Result(it)
            }.onFailure {
                _uiState.value = SpotterResultUiState.Error(it.localizedMessage)
            }
        }
    }

}

