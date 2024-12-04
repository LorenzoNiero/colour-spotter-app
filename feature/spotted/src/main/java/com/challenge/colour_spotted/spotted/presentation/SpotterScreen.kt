package com.challenge.colour_spotted.spotted.presentation

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.Button
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.challenge.colour_spotted.spotted.R
import com.challenge.colour_spotted.spotted.presentation.model.SpotterActionUiState
import com.challenge.colour_spotted.spotted.presentation.model.SpotterResultUiState
import com.challenge.colour_spotter.camera.CameraPreview
import com.challenge.colour_spotter.camera.RequiresCameraPermission
import com.challenge.colour_spotter.common.domain.model.ColorModel
import com.challenge.colour_spotter.ui.component.ColorCell
import com.challenge.colour_spotter.ui.component.TopBar
import com.challenge.colour_spotter.ui.theme.ColourSpotterTheme
import com.challenge.colour_spotter.ui.R as R_UI

@Composable
fun SpotterScreen (
    navController: NavHostController,
    viewModel: SpotterViewModel = hiltViewModel()
) {
    val uiState = viewModel.resultUiState.collectAsState()
    val actionUiState = viewModel.actionUIResult.collectAsState()

    SpotterContent(
        resultUiState = uiState.value,
        actionUiState = actionUiState.value,
        processFrame = {
            viewModel.recognizeColor(it)
        }
    )
}

@Composable
private fun SpotterContent(
    resultUiState: SpotterResultUiState,
    actionUiState: SpotterActionUiState,
    processFrame : (String) -> Unit
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

            RequiresCameraPermission {
                CameraPreview(){ hexColor ->
                    processFrame(hexColor)
                }

                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawRect(
                        color = Color.Black.copy(alpha = 0.5f), // Semi-transparent black overlay
                        size = size // Fill the entire canvas
                    )
                    //create a hole
                    drawCircle(
                        color = Color.Transparent,
                        radius = size.minDimension / 8,
                        center = Offset(size.width / 2, size.height / 2),
                        blendMode = BlendMode.Clear // Use Clear blend mode to create a hole
                    )
                }

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
                                Row(
                                    modifier = Modifier.padding(dimensionResource(R_UI.dimen.small)),
                                    horizontalArrangement = Arrangement.spacedBy(
                                        dimensionResource(
                                            R_UI.dimen.small
                                        )
                                    )
                                ) {
                                    TextField(
                                        value = actionUiState.text,
                                        onValueChange = { actionUiState.onUpdate(it) },
                                        enabled = resultUiState !is SpotterResultUiState.Loading,
                                        modifier = Modifier.weight(1f),
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                        keyboardActions = KeyboardActions(
                                            onDone = { actionUiState.onClickAction() }
                                        ),
                                    )
                                    Button(
                                        onClick = { actionUiState.onClickAction() },
                                        enabled = resultUiState !is SpotterResultUiState.Loading
                                    ) {
                                        Text(stringResource(R.string.search_button))
                                    }
                                }
                                Button(
                                    onClick = {
                                        //todo
                                    },
                                    modifier = Modifier
                                ) {
                                    Text("Start/Stop")
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

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun Preview_SpotterScreen_Loading() {
    ColourSpotterTheme {
        SpotterContent(
            resultUiState = SpotterResultUiState.Loading,
            actionUiState = SpotterActionUiState.Action(
                text = "",
                onUpdate = {},
                onClickAction = {}
            ),
            processFrame = { }
        )
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun Preview_SpotterScreen_Idle() {
    ColourSpotterTheme {
        SpotterContent(
            resultUiState = SpotterResultUiState.Idle,
            actionUiState = SpotterActionUiState.Action(
                text = "",
                onUpdate = {},
                onClickAction = {}
            ),
            processFrame = { }
        )
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun Preview_SpotterScreen_Error() {
    ColourSpotterTheme {
        SpotterContent(
            resultUiState = SpotterResultUiState.Error("Message error", {}),
            actionUiState = SpotterActionUiState.Action(
                text = "",
                onUpdate = {},
                onClickAction = {}
            ),
            processFrame = { }
        )
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
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
                text = "",
                onUpdate = {},
                onClickAction = {}
            ),
            processFrame = { }
        )
    }
}

