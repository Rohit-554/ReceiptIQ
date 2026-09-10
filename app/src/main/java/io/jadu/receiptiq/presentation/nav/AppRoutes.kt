package io.jadu.receiptiq.presentation.nav

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed class AppRoutes: NavKey {

    @Serializable
    data object HomePage : AppRoutes()

    @Serializable
    data object CaptureScreen : AppRoutes()
}