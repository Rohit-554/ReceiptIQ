package io.jadu.receiptiq.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.jadu.receiptiq.data.camera.CameraController
import io.jadu.receiptiq.domain.model.CapturedReceipt
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class ReceiptScannerViewModel(
    val cameraController: CameraController
) : ViewModel() {

    private val _uiState = MutableStateFlow<ReceiptScannerUiState>(ReceiptScannerUiState())
    val uiState: StateFlow<ReceiptScannerUiState> = _uiState.asStateFlow()


    fun captureReceipt() {
        if(_uiState.value.isCapturing) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isCapturing = true, error = null)
            runCatching { cameraController.captureReceipt()}
                .onSuccess {
                    _uiState.value = ReceiptScannerUiState(
                        capturedReceipt = CapturedReceipt(it)
                    )
                }
                .onFailure {
                    _uiState.value = ReceiptScannerUiState(
                        error = it.message
                    )
                }
        }
    }

    fun retake() {
        _uiState.value = ReceiptScannerUiState()
    }
}