package io.jadu.receiptiq.data.ocr

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import io.jadu.receiptiq.domain.ocr.ocr.ReceiptTextRecognizer
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class MlKitReceiptTextRecognizer(
    private val context: Context
) : ReceiptTextRecognizer {
    override suspend fun recognizeText(imagePath: String): String =
        suspendCancellableCoroutine { continuation ->
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

            fun closeRecognizer() {
                recognizer.close()
            }

            try {
                val image = InputImage.fromFilePath(context, Uri.fromFile(File(imagePath)))
                recognizer.process(image)
                    .addOnSuccessListener { result ->
                        closeRecognizer()
                        if (continuation.isActive) continuation.resume(result.text)
                    }
                    .addOnFailureListener { exception ->
                        closeRecognizer()
                        if (continuation.isActive) continuation.resumeWithException(exception)
                    }
                    .addOnCanceledListener {
                        closeRecognizer()
                        if (continuation.isActive) continuation.cancel()
                    }
            } catch (exception: Exception) {
                closeRecognizer()
                if (continuation.isActive) continuation.resumeWithException(exception)
            }

            continuation.invokeOnCancellation {
                recognizer.close()
            }
        }
}
