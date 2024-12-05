package com.challenge.colour_spotted.spotted.presentation

import android.util.Log
import androidx.lifecycle.AtomicReference
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.colour_spotted.spotted.BuildConfig
import com.challenge.colour_spotted.spotted.presentation.model.SpotterActionUiState
import com.challenge.colour_spotted.spotted.presentation.model.SpotterResultUiState
import com.challenge.colour_spotted.spotted.presentation.model.TextFieldAction
import com.challenge.colour_spotter.common.addHashIfNeeded
import com.challenge.colour_spotter.domain.usecase.GetColourInformationByHexUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds


@OptIn(FlowPreview::class)
@HiltViewModel
class SpotterViewModel @Inject constructor(
    private val getColourInformationByHexUseCase : GetColourInformationByHexUseCase
) : ViewModel() {

    private val enableTextField = false //BuildConfig.DEBUG

    private val _resultUiState : MutableStateFlow<SpotterResultUiState> = MutableStateFlow(SpotterResultUiState.Idle)
    internal val resultUiState: StateFlow<SpotterResultUiState> by lazy { _resultUiState.asStateFlow() }

    private val _actionUIResult: MutableStateFlow<SpotterActionUiState> = MutableStateFlow(
        SpotterActionUiState.Action(
            textField = TextFieldAction(
                text = "",
                onUpdate = { newText -> updateText(newText) },
                onClickAction = {
                    performAction()
                }
            ).takeIf {
                enableTextField
            },
            onStartOrStopClickAction = {
                viewModelScope.launch(Dispatchers.IO) {
                    _isRunningUiState.emit(_isRunningUiState.value.not())
                }
            }

        )
    )
    val actionUIResult: StateFlow<SpotterActionUiState> = _actionUIResult.asStateFlow()

    private val _isRunningUiState : MutableStateFlow<Boolean> = MutableStateFlow(true)
    internal val isRunningUiState: StateFlow<Boolean> by lazy { _isRunningUiState.asStateFlow() }

    private val _colorName: MutableStateFlow<String?> = MutableStateFlow(null)
    private val colorName: StateFlow<String?> by lazy { _colorName.asStateFlow() }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            colorName.sample(2000L).collectLatest { bitmap ->
                bitmap?.let { hexCode ->
                    Log.e("", "hexCode: $hexCode")
                    fetchColorDescription( hexCode )
                }
            }
        }
    }


    private val fetchJob: AtomicReference<Job?> by lazy { AtomicReference(null) }
    private fun fetchColorDescription(hexColor : String) {
        if (fetchJob.get()?.isActive == true) {
            //skip
            return
        }
        _resultUiState.value = SpotterResultUiState.Loading
        fetchJob.set(
            viewModelScope.launch(Dispatchers.IO) {
                _isRunningUiState.emit(false)
                getColourInformationByHexUseCase(hexColor).onSuccess {
                    _resultUiState.value = SpotterResultUiState.Result(it)
                }.onFailure {
                    _resultUiState.value = SpotterResultUiState.Error(
                        it.localizedMessage,
                        onRetry = { performAction() }
                    )
                }
            }
        )
    }

    private fun updateText(newText: String) {
        val actionState = _actionUIResult.value
        if (actionState is SpotterActionUiState.Action) {
            _actionUIResult.value =
                actionState.copy(textField = actionState.textField?.copy(text = newText))
        }
    }

    private fun performAction() {
        viewModelScope.launch {
            val actionState = _actionUIResult.value
            if ( actionState is SpotterActionUiState.Action && actionState.textField != null) {
                fetchColorDescription(actionState.textField.text.trim().addHashIfNeeded())
            }
        }
    }

    fun recognizeColor(hexColor : String) {
        viewModelScope.launch {
            _colorName.emit(hexColor)
        }
    }

}

