package com.challenge.colour_spotted.spotted.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.challenge.colour_spotted.spotted.R
import com.challenge.colour_spotted.spotted.presentation.model.SpotterActionUiState
import com.challenge.colour_spotted.spotted.presentation.model.SpotterResultUiState
import com.challenge.colour_spotted.spotted.presentation.model.TextFieldAction
import com.challenge.colour_spotter.camera.ColorQuantizerPreview
import com.challenge.colour_spotter.common.domain.model.ColorModel
import com.challenge.colour_spotter.ui.component.ColorCell
import com.challenge.colour_spotter.ui.component.TopBar
import com.challenge.colour_spotter.ui.theme.ColourSpotterTheme
import com.challenge.colour_spotter.ui.R as R_UI

@Composable
fun SpotterScreen(
    navController: NavHostController,
    viewModel: SpotterViewModel = hiltViewModel()
) {
    val uiState = viewModel.resultUiState.collectAsState()
    val actionUiState = viewModel.actionUIResult.collectAsState()
    val isRunning = viewModel.isRunningUiState.collectAsState()

    SpotterContent(
        resultUiState = uiState.value,
        actionUiState = actionUiState.value,
        onColorCaptured = {
            viewModel.recognizeColor(it)
        },
        isRunning = isRunning.value
    )
}

@Composable
private fun SpotterContent(
    resultUiState: SpotterResultUiState,
    actionUiState: SpotterActionUiState,
    onColorCaptured: (String) -> Unit,
    isRunning: Boolean
) {
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(id = R_UI.string.app_name),
                onBackClick = null,
                actions = {
                    IconButton(onClick = {
                        //TODO
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.List,
                            contentDescription = "none"
                        )
                    }
                }
            )
        }) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {

            ColorQuantizerPreview(
                enableScanning = isRunning,
                onColorCaptured = { hexColor ->
                    onColorCaptured(hexColor)
                }
            ) {

                Column(
                    Modifier
                        .align(Alignment.TopCenter)
                        .padding(vertical = dimensionResource(R_UI.dimen.normal))
                        .padding(horizontal = dimensionResource(R_UI.dimen.large))
                ) {

                    when (resultUiState) {
                        is SpotterResultUiState.Error -> {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = resultUiState.message
                                        ?: stringResource(R.string.error_unknown)
                                )
                                ButtonRetry() {
                                    resultUiState.onRetry()
                                }
                            }
                        }

                        SpotterResultUiState.Idle -> {

                        }

                        SpotterResultUiState.Loading -> {
                            CircularProgressIndicator()
                        }

                        is SpotterResultUiState.Result -> {
                            ColorCell(
                                color = resultUiState.color,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                Column(
                    Modifier
                        .align(Alignment.BottomStart)
                        .padding(bottom = dimensionResource(R_UI.dimen.small))
                        .padding(horizontal = dimensionResource(R_UI.dimen.normal))
                ) {
                    when (actionUiState) {
                        is SpotterActionUiState.Action -> {
                            Column {
                                val enableButton = when (resultUiState) {
                                    SpotterResultUiState.Loading -> false
                                    else ->  true
                                }

                                actionUiState.textField?.let { textFieldState ->
                                    Row(
                                        modifier = Modifier
                                            .padding(bottom = dimensionResource(R_UI.dimen.normal))
                                            .padding(horizontal = dimensionResource(R_UI.dimen.small)),
                                        horizontalArrangement = Arrangement.spacedBy(
                                            dimensionResource(
                                                R_UI.dimen.small
                                            )
                                        )
                                    ) {

                                        TextField(
                                            value = textFieldState.text,
                                            onValueChange = { textFieldState.onUpdate(it) },
                                            enabled = enableButton,
                                            modifier = Modifier.weight(1f),
                                            singleLine = true,
                                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                            keyboardActions = KeyboardActions(
                                                onDone = { textFieldState.onClickAction() }
                                            ),
                                        )
                                        Button(
                                            onClick = { textFieldState.onClickAction() },
                                            enabled = enableButton
                                        ) {
                                            Text(stringResource(R.string.search_button))
                                        }
                                    }
                                }
                                Button(
                                    onClick = {
                                        actionUiState.onStartOrStopClickAction()
                                    },
//                                    enabled = enableButton,
                                    modifier = Modifier.fillMaxWidth(),
                                    contentPadding = PaddingValues(horizontal = dimensionResource(R_UI.dimen.button_large_horizontal_padding), vertical = dimensionResource(R_UI.dimen.button_large_vertical_padding))
                                ) {
                                    if (isRunning) {
                                        Text(stringResource(R.string.stop_button))
                                    }
                                    else{
                                        Text(stringResource(R.string.start_button))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ButtonRetry(onClick: () -> Unit) {
    Button(
        onClick = onClick
    ) {
        Text(text = stringResource(R.string.retry_button))
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun Preview_SpotterScreen_Loading() {
    ColourSpotterTheme {
        SpotterContent(
            resultUiState = SpotterResultUiState.Loading,
            actionUiState = SpotterActionUiState.Action(
                null,
                {}
            ),
            onColorCaptured = { },
            isRunning = false
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun Preview_SpotterScreen_Idle() {
    ColourSpotterTheme {
        SpotterContent(
            resultUiState = SpotterResultUiState.Idle,
            actionUiState = SpotterActionUiState.Action(
                textField = TextFieldAction(
                    text = "",
                    onUpdate = {},
                    onClickAction = {}
                ),
                {}
            ),
            onColorCaptured = { },
            isRunning = false
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun Preview_SpotterScreen_Error() {
    ColourSpotterTheme {
        SpotterContent(
            resultUiState = SpotterResultUiState.Error("Message error", {}),
            actionUiState = SpotterActionUiState.Action(
                null,
                {}
            ),
            onColorCaptured = { },
            isRunning = false
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun Preview_SpotterScreen_Result() {
    ColourSpotterTheme {
        SpotterContent(
            resultUiState = SpotterResultUiState.Result(
                ColorModel(
                    name = "Red",
                    hex = "#FF0000"
                )
            ),
            actionUiState = SpotterActionUiState.Action(
                textField = TextFieldAction(
                    text = "",
                    onUpdate = {},
                    onClickAction = {}
                ),
                {}
            ),
            onColorCaptured = { },
            isRunning = false
        )
    }
}

