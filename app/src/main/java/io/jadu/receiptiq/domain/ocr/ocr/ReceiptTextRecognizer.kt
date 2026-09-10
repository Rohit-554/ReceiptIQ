package io.jadu.receiptiq.domain.ocr.ocr

interface ReceiptTextRecognizer {
    suspend fun recognizeText(imagePath: String): String
}
