package io.jadu.receiptiq.di

import android.content.Context
import io.jadu.receiptiq.data.camera.CameraController
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
}
