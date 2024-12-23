package com.challenge.colour_spotter.camera

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.UseCaseGroup
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.challenge.colour_spotter.camera.analyzer.ColorAnalyzer_Palette_Version3
import com.challenge.colour_spotter.camera.analyzer.ColorAnalyzer_QuantizerAsync_Version5
import kotlinx.coroutines.launch
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun BoxScope.ColorQuantizerPreview(
    enableAnalysisImage : Boolean,
    onColorCaptured: (String) -> Unit,
    content: @Composable BoxScope.() -> Unit
) {

    RequiresCameraPermission {
        CameraPreviewAndAnalysis(
            enableAnalysisImage = enableAnalysisImage,
            onColorCaptured = onColorCaptured
        )

        // TODO: to removed, alternative way to create a hole
//        Canvas(modifier = Modifier
//            .fillMaxSize()
//        ) {
//            drawRect(
//                color = Color.Black.copy(alpha = 0.5f), // Semi-transparent black overlay
//                size = size // Fill the entire canvas
//            )
//            //create a hole
//            drawCircle(
//                color = Color.Transparent,
//                radius = size.minDimension * Constants.percentDivisionImage,
//                center = Offset(size.width / 2, size.height / 2),
//                blendMode = BlendMode.Clear // Use Clear blend mode to create a hole
//            )
//        }

        content()
    }
}

@Composable
internal fun CameraPreviewAndAnalysis(
    enableAnalysisImage : Boolean,
    onColorCaptured: (String) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember {
        PreviewView(context).apply {
            this.scaleType = PreviewView.ScaleType.FILL_CENTER
            this.overlay.add(HoleDrawable())
        }
    }
    val previewUseCase = remember {
        Preview.Builder().build().also {
            it.surfaceProvider = previewView.surfaceProvider
        }
    }

    val cameraSelector = remember {
        CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()
    }

    val cameraExecutor = remember {
        Executors.newSingleThreadExecutor()
    }

    val colorQuantizerAnalyzer = remember {
        ColorAnalyzer_QuantizerAsync_Version5 (
            isEnable = enableAnalysisImage
        )
        { hexColor ->
            coroutineScope.launch {
                onColorCaptured(hexColor)
            }
        }
    }

    val imageAnalysis = remember {
        ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also {
                it.setAnalyzer(cameraExecutor, colorQuantizerAnalyzer)
            }
    }

    LaunchedEffect (enableAnalysisImage){
        colorQuantizerAnalyzer.enable = enableAnalysisImage
    }

    AndroidView(
        modifier = Modifier,
        factory = {
            coroutineScope.launch {
                val cameraProvider = context.getCameraProvider()

                val useCaseGroup = UseCaseGroup.Builder()
                    .addUseCase(previewUseCase)
                    .addUseCase(imageAnalysis)
                    .build()

                runCatching {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        useCaseGroup
                    )
                }
            }
            previewView
        }
    )
}

private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also { future ->
            future.addListener({
                continuation.resume(future.get())
            }, executor)
        }
    }

private val Context.executor: Executor
    get() = ContextCompat.getMainExecutor(this)