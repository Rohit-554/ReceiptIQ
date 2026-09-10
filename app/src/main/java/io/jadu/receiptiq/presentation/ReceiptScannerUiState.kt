package io.jadu.receiptiq.presentation

import io.jadu.receiptiq.domain.model.CapturedReceipt

data class ReceiptScannerUiState(
    val capturedReceipt: CapturedReceipt? = null,
    val isCapturing: Boolean = false,
    val extractedText: String? = null,
    val error: String? = null
)