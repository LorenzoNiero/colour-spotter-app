package com.challenge.colour_spotter.camera

import android.content.Context
import android.util.Rational
import android.view.Surface
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.UseCaseGroup
import androidx.camera.core.ViewPort
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.launch
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun BoxScope.ColorQuantizerPreview(
    onColorCaptured: (String) -> Unit,
    content: @Composable BoxScope.() -> Unit
) {

    RequiresCameraPermission {
        CameraPreviewAndAnalysis(onColorCaptured)

        Canvas(modifier = Modifier
            .fillMaxSize()
        ) {
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

        content()
    }
}

@Composable
internal fun CameraPreviewAndAnalysis(
    onFrameCaptured: (String) -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember {
        PreviewView(context).apply {
            this.scaleType = PreviewView.ScaleType.FILL_CENTER
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

//    var colorAnalyser: MutableState<ColorQuantizerAnalyzer?> = remember {
//        mutableStateOf(
//           null
//        )
//    }

    var aspectRatio by remember { mutableFloatStateOf(1f) }

    val imageAnalysis = remember {
        ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_BLOCK_PRODUCER)
            .build()
            .also {
                val analyzer = ColorQuantizerAnalyzer { hexColor ->
                    onFrameCaptured(hexColor)
                }
                it.setAnalyzer(cameraExecutor, analyzer)
//                colorAnalyser.value = analyzer
            }
    }

    val coroutineScope = rememberCoroutineScope()

    AndroidView(
        modifier = Modifier.onGloballyPositioned { coordinates ->
            val size = coordinates.size
            aspectRatio = size.width.toFloat() / size.height.toFloat()
        },
        factory = {
            coroutineScope.launch {
                val cameraProvider = context.getCameraProvider()

//                val viewport = ViewPort.Builder(
//                    Rational(16, 9),
//                    Surface.ROTATION_0
//                ).build()

                val useCaseGroup = UseCaseGroup.Builder()
                    .addUseCase(previewUseCase)
                    .addUseCase(imageAnalysis)
//                    .setViewPort(viewport)
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

