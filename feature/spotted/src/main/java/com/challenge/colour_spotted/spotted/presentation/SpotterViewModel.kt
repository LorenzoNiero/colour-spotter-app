package com.challenge.colour_spotted.spotted.presentation

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.colour_spotted.spotted.presentation.model.SpotterActionUiState
import com.challenge.colour_spotted.spotted.presentation.model.SpotterResultUiState
import com.challenge.colour_spotter.common.addHashIfNeeded
import com.challenge.colour_spotter.domain.usecase.GetColourInformationByHexUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.time.Duration


@OptIn(FlowPreview::class)
@HiltViewModel
class SpotterViewModel @Inject constructor(
    private val getColourInformationByHexUseCase : GetColourInformationByHexUseCase
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

    private val _colorName: MutableStateFlow<String?> = MutableStateFlow(null)
    val colorName: StateFlow<String?> = _colorName.asStateFlow()

    init {

        viewModelScope.launch(Dispatchers.IO) {
            colorName.sample(2000).collectLatest { bitmap ->
                bitmap?.let { hexCode ->
                    Log.e("", "hexCode: $hexCode")
                    fetchColorDescription( hexCode )
                }
            }
        }

    }

    private suspend fun fetchColorDescription(hexColor : String) {
        _resultUiState.value = SpotterResultUiState.Loading
        withContext(Dispatchers.IO) {
            getColourInformationByHexUseCase(hexColor).onSuccess {
                _resultUiState.value = SpotterResultUiState.Result(it)
            }.onFailure {
                _resultUiState.value = SpotterResultUiState.Error(
                    it.localizedMessage,
                    onRetry = { performAction() }
                )
            }
        }
    }

    private fun updateText(newText: String) {
        val actionState = _actionUIResult.value
        if ( actionState is SpotterActionUiState.Action) {
            _actionUIResult.value = actionState.copy (text = newText)
        }
    }

    private fun performAction() {
        viewModelScope.launch {
            val actionState = _actionUIResult.value
            if ( actionState is SpotterActionUiState.Action) {
                fetchColorDescription(actionState.text.trim().addHashIfNeeded())
            }
        }
    }

    fun processFrame(hexColor: String) {
        viewModelScope.launch (Dispatchers.IO) {
            _colorName.value = hexColor
        }
    }

    fun analyzeColor(bitmap: Bitmap): Int {
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

        val colorCount = mutableMapOf<Int, Int>()
        pixels.forEach { color ->
            colorCount[color] = (colorCount[color] ?: 0) + 1
        }

        return colorCount.maxByOrNull { it.value }?.key ?: Color.BLACK
    }
}

