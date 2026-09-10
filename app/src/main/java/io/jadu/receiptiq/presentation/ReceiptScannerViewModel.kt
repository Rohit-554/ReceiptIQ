package io.jadu.receiptiq.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.jadu.receiptiq.presentation.camera.CameraController
import io.jadu.receiptiq.domain.model.CapturedReceipt
import io.jadu.receiptiq.domain.ocr.ocr.ReceiptTextRecognizer
import io.jadu.receiptiq.presentation.scanner.ReceiptImageCapturer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class ReceiptScannerViewModel(
    val receiptImageCapturer: ReceiptImageCapturer,
    val receiptTextRecognizer: ReceiptTextRecognizer
) : ViewModel() {

    private val _uiState = MutableStateFlow<ReceiptScannerUiState>(ReceiptScannerUiState())
    val uiState: StateFlow<ReceiptScannerUiState> = _uiState.asStateFlow()


    fun captureReceipt() {
        if (_uiState.value.isCapturing) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isCapturing = true, error = null)
            val path = runCatching { receiptImageCapturer.captureReceipt() }
                .getOrElse { error ->
                    _uiState.value = ReceiptScannerUiState(
                        error = error.message ?: "Unable to capture image"
                    )
                    return@launch
                }

            val textResult = runCatching { receiptTextRecognizer.recognizeText(path) }
            textResult.onSuccess { extractedText ->
                _uiState.value = ReceiptScannerUiState(
                    capturedReceipt = CapturedReceipt(path),
                    extractedText = extractedText
                )
            }.onFailure { error ->
                _uiState.value = ReceiptScannerUiState(
                    capturedReceipt = CapturedReceipt(path),
                    error = error.message ?: "Unable to read the receipt"
                )
            }
        }
    }

    fun retake() {
        _uiState.value = ReceiptScannerUiState()
    }
}