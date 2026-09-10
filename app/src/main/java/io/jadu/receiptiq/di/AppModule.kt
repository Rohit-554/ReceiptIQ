package io.jadu.receiptiq.di

import android.content.Context
import io.jadu.receiptiq.data.ocr.MlKitReceiptTextRecognizer
import io.jadu.receiptiq.domain.ocr.ocr.ReceiptTextRecognizer
import io.jadu.receiptiq.presentation.camera.CameraController
import io.jadu.receiptiq.presentation.scanner.ReceiptImageCapturer
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Module
@ComponentScan("io.jadu.receiptiq.presentation")
class AppModule {
    @Single
    fun cameraController(@Provided context: Context): CameraController {
        return CameraController(context)
    }

    @Single
    fun receiptImageCapturer(cameraController: CameraController): ReceiptImageCapturer {
        return cameraController
    }

    @Single
    fun receiptTextRecognizer(@Provided context: Context): ReceiptTextRecognizer {
        return MlKitReceiptTextRecognizer(context)
    }
}
