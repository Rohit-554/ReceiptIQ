package io.jadu.receiptiq.presentation.nav

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import io.jadu.receiptiq.presentation.HomeScreen
import io.jadu.receiptiq.presentation.scanner.ReceiptScannerScreen

@Composable
fun AppNavigation() {
    val backstack = rememberNavBackStack(AppRoutes.HomePage)

    NavDisplay(
        backStack = backstack,
        onBack = { backstack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<AppRoutes.HomePage> {
                HomeScreen (
                    onScanReceiptClicked = {
                        backstack.add(AppRoutes.CaptureScreen)
                    }
                )
            }

            entry<AppRoutes.CaptureScreen> {
                ReceiptScannerScreen()
            }
        }
    )
}
