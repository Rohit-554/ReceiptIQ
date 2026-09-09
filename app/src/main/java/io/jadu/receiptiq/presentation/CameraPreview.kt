package io.jadu.receiptiq.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.camera.view.PreviewView
import androidx.compose.runtime.remember
import androidx.lifecycle.LifecycleOwner
import io.jadu.receiptiq.data.camera.CameraController

@Composable
fun CameraPreview(
    cameraController: CameraController,
    lifecycleOwner: LifecycleOwner,
    onCapture: () -> Unit,
    isCapturing: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val previewView = remember { PreviewView(context) }
    DisposableEffect(lifecycleOwner) {
        cameraController.startCamera(lifecycleOwner, previewView)
        onDispose { cameraController.stopCamera() }
    }
    Box(modifier = modifier) {
        AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())
        Button(
            onClick = onCapture,
            enabled = !isCapturing,
            modifier = Modifier.align(Alignment.BottomCenter).padding(24.dp)
        ) {
            if (isCapturing) CircularProgressIndicator() else Text("Capture receipt")
        }
    }
}
