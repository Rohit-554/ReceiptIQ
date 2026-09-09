package io.jadu.receiptiq.presentation

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil.compose.AsyncImage
import io.jadu.receiptiq.data.camera.CameraController

@Composable
fun ReceiptScannerScreen(
    uiState: ReceiptScannerUiState,
    cameraController: CameraController,
    onCapture: () -> Unit,
    onRetake: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var hasCameraPermission by remember {
        mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
    }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        hasCameraPermission = it
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when {
            uiState.capturedReceipt != null -> {
                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    AsyncImage(
                        model = uiState.capturedReceipt.imagePath,
                        contentDescription = "Captured receipt",
                        modifier = Modifier.fillMaxWidth().weight(1f)
                    )
                    Button(onClick = onRetake, modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
                        Text("Retake")
                    }
                }
            }
            !hasCameraPermission -> Text("Camera permission is required to scan a receipt")
            else -> {
                CameraPreview(
                    cameraController = cameraController,
                    lifecycleOwner = lifecycleOwner,
                    onCapture = onCapture,
                    isCapturing = uiState.isCapturing,
                    modifier = Modifier.fillMaxSize()
                )
                uiState.error?.let { error ->
                    Text(error, color = MaterialTheme.colorScheme.error, modifier = Modifier.align(Alignment.TopCenter).padding(16.dp))
                }
            }
        }
    }
}
