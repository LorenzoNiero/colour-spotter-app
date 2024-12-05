package com.challenge.colour_spotted.list.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.challenge.colour_spotted.list.presentation.model.ListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class ListViewModel @Inject constructor(

) : ViewModel() {

    private val _listUiState : MutableStateFlow<ListUiState> = MutableStateFlow(ListUiState.Loading)
    internal val listUiState: StateFlow<ListUiState> by lazy { _listUiState.asStateFlow() }


}

