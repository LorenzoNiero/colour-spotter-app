package com.challenge.colour_spotter.camera

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
fun CameraPreview(onFrameCaptured: (String) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context) }
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

    val colorAnalyser = remember {
        ColorQuantizerAnalyzer{ hexColor ->
            onFrameCaptured(hexColor)
        }
    }

    val imageAnalysis = remember {
        ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_BLOCK_PRODUCER)
            .build()
            .also { it.setAnalyzer(cameraExecutor, colorAnalyser) }
    }

    val coroutineScope = rememberCoroutineScope()

    AndroidView(
        factory = {
            coroutineScope.launch {
                val cameraProvider = context.getCameraProvider()

                runCatching {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        previewUseCase,
                        imageAnalysis
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

