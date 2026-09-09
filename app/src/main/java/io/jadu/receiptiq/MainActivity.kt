package io.jadu.receiptiq

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.jadu.receiptiq.data.camera.CameraController
import io.jadu.receiptiq.presentation.ReceiptScannerScreen
import io.jadu.receiptiq.presentation.ReceiptScannerViewModel
import io.jadu.receiptiq.ui.theme.ReceiptIQTheme
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReceiptIQTheme {
                val cameraController = koinInject<CameraController>()
                val scannerViewModel = koinViewModel<ReceiptScannerViewModel>()
                ReceiptScannerScreen(
                    uiState = scannerViewModel.uiState.collectAsStateWithLifecycle().value,
                    cameraController = cameraController,
                    onCapture = scannerViewModel::captureReceipt,
                    onRetake = scannerViewModel::retake
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ReceiptIQTheme {
        Greeting("Android")
    }
}