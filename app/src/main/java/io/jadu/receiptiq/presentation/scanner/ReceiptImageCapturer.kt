package io.jadu.receiptiq.presentation.scanner

interface ReceiptImageCapturer {
    suspend fun captureReceipt(): String
}
