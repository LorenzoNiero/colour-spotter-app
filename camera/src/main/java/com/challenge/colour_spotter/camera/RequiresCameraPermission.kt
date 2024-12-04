package com.challenge.colour_spotter.camera

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BoxScope.RequiresCameraPermission(
    content: @Composable () -> Unit
) {
    if (LocalInspectionMode.current) {
        content()
    }
    else {
        val cameraPermissionState = rememberPermissionState(
            android.Manifest.permission.CAMERA
        )

        if (cameraPermissionState.status.isGranted) {
            content()
        } else {
            MessagePermission(cameraPermissionState.status.shouldShowRationale){
                cameraPermissionState.launchPermissionRequest()
            }
        }
    }
}

@Composable
private fun BoxScope.MessagePermission(shouldShowRationale: Boolean, launchPermissionRequest : ()->Unit) {
    Column(
        modifier = Modifier
            .padding(dimensionResource(com.challenge.colour_spotter.ui.R.dimen.large))
            .align(
                Alignment.Center
            ),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(com.challenge.colour_spotter.ui.R.dimen.small)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val textToShow = if (shouldShowRationale) {
            // If the user has denied the permission but the rationale can be shown,
            // then gently explain why the app requires this permission
            stringResource(R.string.permission_camera_important_message)
        } else {
            // If it's the first time the user lands on this feature, or the user
            // doesn't want to be asked again for this permission, explain that the
            // permission is required
            stringResource(R.string.permission_camera_message)
        }
        Text(
            textToShow,
            textAlign = TextAlign.Center
        )
        Button(onClick = { launchPermissionRequest() }) {
            Text(stringResource(R.string.request_permission))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewRequiresCameraPermission() {
    Box(modifier = Modifier.fillMaxHeight()) {
        MessagePermission(true, {})
    }
}
