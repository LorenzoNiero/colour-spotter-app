package com.challenge.colour_spotted.spotted.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.colour_spotted.spotted.presentation.model.SpotterActionUiState
import com.challenge.colour_spotted.spotted.presentation.model.SpotterResultUiState
import com.challenge.colour_spotted.spotted.presentation.model.TextFieldAction
import com.challenge.colour_spotter.common.addHashIfNeeded
import com.challenge.colour_spotter.domain.usecase.GetColourInformationByHexUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

/**
 * ViewModel for the Spotter screen.
 *
 * This ViewModel is responsible for managing the state and logic related to color spotting,
 *
 * It exposes the following state flows:
 * - `resultUiState`: Represents the current state of the color spotting result.
 * - `actionUIResult`: Represents the current state of the UI actions.
 * - `isRunningUiState`: Indicates whether the color spotting process is running.
 *
 * It also provides the following public function:
 * - `detectedColor`: To be called when a color is detected by the camera.
 */
@HiltViewModel
class SpotterViewModel @Inject constructor(
    private val getColourInformationByHexUseCase : GetColourInformationByHexUseCase
) : ViewModel() {

    private val enableTextField = false // BuildConfig.DEBUG

    private val _resultUiState : MutableStateFlow<SpotterResultUiState> = MutableStateFlow(SpotterResultUiState.Idle)
    internal val resultUiState: StateFlow<SpotterResultUiState> by lazy { _resultUiState.asStateFlow() }

    private val _actionUIResult: MutableStateFlow<SpotterActionUiState> = MutableStateFlow(
        SpotterActionUiState.Action(
            textField = TextFieldAction(
                text = "",
                onUpdate = { newText -> updateText(newText) },
                onClickAction = {
                    fetchColorInformationFromInput()
                }
            ).takeIf {
                enableTextField
            },
            onStartOrStopClickAction = {
                viewModelScope.launch(Dispatchers.IO) {
                    _isImageAnalysisEnabledState.emit(_isImageAnalysisEnabledState.value.not())
                }
            }

        )
    )
    val actionUIResult: StateFlow<SpotterActionUiState> = _actionUIResult.asStateFlow()

    private val _isImageAnalysisEnabledState : MutableStateFlow<Boolean> = MutableStateFlow(false)
    internal val isImageAnalysisEnabledState: StateFlow<Boolean> by lazy { _isImageAnalysisEnabledState.asStateFlow() }

    private val _colorHexCode: MutableStateFlow<String?> = MutableStateFlow(null)
    private val colorHexColor: StateFlow<String?> by lazy { _colorHexCode.asStateFlow() }

    init {
        viewModelScope.launch {
            colorHexColor.collectLatest { bitmap ->
                bitmap?.let { hexCode ->
                    Log.e("", "hexCode: $hexCode")
                    withContext(Dispatchers.IO) {
                        fetchColorDescription(hexCode)
                    }
                }
            }
        }
    }

    private val fetchJob: AtomicReference<Job?> by lazy { AtomicReference(null) }
    private suspend fun fetchColorDescription(hexColor : String) {
        if (fetchJob.get()?.isActive == true) {
            //skip
            return
        }
        _resultUiState.emit(SpotterResultUiState.Loading)

        val newJob = viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.IO) {
                _isImageAnalysisEnabledState.emit(false)
                getColourInformationByHexUseCase(hexColor).onSuccess {
                    _resultUiState.emit(SpotterResultUiState.Result(it))
                }.onFailure {
                    _resultUiState.emit(SpotterResultUiState.Error(
                        it.localizedMessage,
                        onRetry = { fetchColorInformationFromInput() }
                    )
                    )
                }
            }

        }
        fetchJob.set(newJob)
        newJob.join()
    }

    private fun updateText(newText: String) {
        val actionState = _actionUIResult.value
        if (actionState is SpotterActionUiState.Action) {
            _actionUIResult.value =
                actionState.copy(textField = actionState.textField?.copy(text = newText))
        }
    }

    private fun fetchColorInformationFromInput() {
        viewModelScope.launch {
            val actionState = _actionUIResult.value
            if ( actionState is SpotterActionUiState.Action && actionState.textField != null) {
                fetchColorDescription(actionState.textField.text.trim().addHashIfNeeded())
            }
        }
    }

    fun detectedColor(hexColor : String) {
        viewModelScope.launch {
            _colorHexCode.emit(hexColor)
        }
    }

}

