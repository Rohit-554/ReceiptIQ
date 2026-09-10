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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.lifecycle.LifecycleOwner
import io.jadu.receiptiq.presentation.camera.CameraController

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
        Surface(
            onClick = onCapture,
            enabled = !isCapturing,
            shape = CircleShape,
            color = Color.White,
            shadowElevation = 6.dp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(24.dp)
                .size(76.dp)
                .semantics { contentDescription = "Capture receipt" }
        ) {
            Box(contentAlignment = Alignment.Center) {
                if (isCapturing) {
                    CircularProgressIndicator()
                } else {
                    Surface(
                        modifier = Modifier.size(60.dp),
                        shape = CircleShape,
                        color = Color(0xFFE53935)
                    ) {}
                }
            }
        }
    }
}
