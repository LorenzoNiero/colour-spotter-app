package com.challenge.colour_spotted.list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.colour_spotted.list.presentation.model.ListUiState
import com.challenge.colour_spotter.common.domain.model.ColorModel
import com.challenge.colour_spotter.domain.usecase.DeleteColorUseCase
import com.challenge.colour_spotter.domain.usecase.ObserveColorsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ListViewModel @Inject constructor(
    observeColorsUseCase: ObserveColorsUseCase,
    val deleteColorUseCase : DeleteColorUseCase
) : ViewModel() {

    private val _isDescUiState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isDescUiState: StateFlow<Boolean> by lazy { _isDescUiState.asStateFlow() }

    val listUiState = observeColorsUseCase(true).map {
        if (it.isEmpty()) {
            ListUiState.Empty
        } else {
            ListUiState.Result(
                colors = it,
                onDelete = { color ->
                    deleteItem(color)
                },
                onSort = {
                    _isDescUiState.value = _isDescUiState.value.not()
                }
            )
        }
    }.catch {
        ListUiState.Error("${it.message}")
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5_000),
        initialValue = ListUiState.Loading
    )

    private fun deleteItem(color: ColorModel) {
        viewModelScope.launch {
            runCatching {
                deleteColorUseCase(color)
            }
            .onFailure {
                //todo: show error
            }
        }
    }

}

