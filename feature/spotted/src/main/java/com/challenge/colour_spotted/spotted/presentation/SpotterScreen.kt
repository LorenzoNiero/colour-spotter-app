package com.challenge.colour_spotted.spotted.presentation

import android.content.res.Configuration
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.challenge.colour_spotted.spotted.R
import com.challenge.colour_spotted.spotted.presentation.model.SpotterActionUiState
import com.challenge.colour_spotted.spotted.presentation.model.SpotterResultUiState
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
        actionUiState = actionUiState.value
    )
}

@Composable
private fun SpotterContent(
    resultUiState: SpotterResultUiState,
    actionUiState: SpotterActionUiState
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

            Column (
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
                                    ?: stringResource(R.string.error_unknown))
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

            Column (
                Modifier
                    .align(Alignment.BottomStart)
                    .padding(bottom = dimensionResource(R_UI.dimen.small))
                    .padding(horizontal = dimensionResource(R_UI.dimen.normal))
            ){
                when (actionUiState) {
                    is SpotterActionUiState.Action -> {
                        Row(
                            modifier = Modifier.padding(dimensionResource(R_UI.dimen.small)),
                            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R_UI.dimen.small))
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
                    }
                }
            }



//            RequiresCameraPermission {
//
////                CameraPreview()
//
//                Button(
//                    modifier = Modifier.align(Alignment.BottomCenter),
//                    onClick = {
////                    startCamera(context, previewView) { image ->
////                        analyzeFrame(image) { color ->
////                            detectedColor.value = color
////                        }
////                    }
//                    }
//                ) {
//                    Text(text = stringResource(R.string.button_start))
//                }
//
//            }
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

//@OptIn(ExperimentalPermissionsApi::class)
//@Composable
//private fun RequiresCameraPermission(
//    content: @Composable () -> Unit
//) {
//    val cameraPermissionState = rememberPermissionState(
//        android.Manifest.permission.CAMERA
//    )
//
//    if (cameraPermissionState.status.isGranted) {
//        content()
//    } else {
//        Column (
//            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.Center,
//        ) {
//            val textToShow = if (cameraPermissionState.status.shouldShowRationale) {
//                // If the user has denied the permission but the rationale can be shown,
//                // then gently explain why the app requires this permission
//                stringResource(R.string.permission_camera_important_message)
//            } else {
//                // If it's the first time the user lands on this feature, or the user
//                // doesn't want to be asked again for this permission, explain that the
//                // permission is required
//                stringResource(R.string.permission_camera_message)
//            }
//            Text(textToShow)
//            Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
//                Text(stringResource(R.string.request_permission))
//            }
//        }
//    }
//}

//@Composable
//fun CameraPreview(
////    previewView: androidx.camera.view.PreviewView
//) {
//
//    val context = LocalContext.current
//    val lifecycleOwner = LocalLifecycleOwner.current
//    val previewView = remember { PreviewView(context) }
//    val detectedColor = remember { mutableStateOf(Color.White) }
//
//
//    AndroidView(
//        factory = { previewView },
//        update = { previewView ->
//            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
//            cameraProviderFuture.addListener({
//                val cameraProvider = cameraProviderFuture.get()
//                val preview = Preview.Builder().build().also {
//                    it.setSurfaceProvider(previewView.surfaceProvider)
//                }
//                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
//
//                try {
//                    cameraProvider.unbindAll()
//                    cameraProvider.bindToLifecycle(
//                        lifecycleOwner,
//                        cameraSelector,
//                        preview
//                    )
//                } catch (e: Exception) {
//                    Log.e("CameraPreview", "Use case binding failed", e)
//                }
//            }, ContextCompat.getMainExecutor(context))
//        },
//        modifier = Modifier.fillMaxSize()
//    )
//}
//
//private fun startCamera(context: Context, previewView: PreviewView, onFrameCaptured: (ImageProxy) -> Unit) {
//    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
//    cameraProviderFuture.addListener({
//        val cameraProvider = cameraProviderFuture.get()
//
//        // Configura Preview
//        val preview: androidx.camera.core.Preview = Preview.Builder().build().also {
//            it.setSurfaceProvider(previewView.surfaceProvider)
//        }
//
//        // Configura Image Analysis
//        val imageAnalyzer = ImageAnalysis.Builder()
//            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
//            .build()
//            .also { it.setAnalyzer(ContextCompat.getMainExecutor(context), onFrameCaptured) }
//
//        // Seleziona la fotocamera posteriore
//        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
//
//        try {
//            cameraProvider.unbindAll()
//            cameraProvider.bindToLifecycle(
//                (context as LifecycleOwner),
//                cameraSelector,
//                preview,
//                imageAnalyzer
//            )
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }, ContextCompat.getMainExecutor(context))
//}
//
//private fun analyzeFrame(image: ImageProxy, onColorDetected: (Int) -> Unit) {
//    val buffer = image.planes[0].buffer
//    val data = ByteArray(buffer.capacity())
//    buffer.get(data)
//
//    val bitmap = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)
//    val canvas = Canvas(bitmap)
//    val paint = Paint()
//    paint.color = extractDominantColor(bitmap)
//    onColorDetected(paint.color)
//
//    image.close()
//}
////
//private fun extractDominantColor(bitmap: Bitmap): Int {
//    val palette = Palette.from(bitmap).generate()
//    return palette.dominantSwatch?.rgb ?: Color.Black.toArgb()
//}


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
            )
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
            )
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
            )
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
            )
        )
    }
}
