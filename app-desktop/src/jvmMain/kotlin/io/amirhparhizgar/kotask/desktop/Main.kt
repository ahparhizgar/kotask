package io.amirhparhizgar.kotask.desktop

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import io.amirhparhizgar.kotask.MultiPaneTasksComponent
import io.amirhparhizgar.kotask.RootContent
import io.amirhparhizgar.kotask.di.AppModule
import org.koin.core.context.startKoin

fun main() {
    val koin =
        startKoin {
            modules(AppModule)
        }.koin

    val lifecycle = LifecycleRegistry()

    val root =
        runOnUiThread {
            koin.get<MultiPaneTasksComponent.Factory>()
                .create(DefaultComponentContext(lifecycle = lifecycle))
        }

    application {
        val windowState = rememberWindowState()

        LifecycleController(lifecycle, windowState)

        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = "KoTask",
        ) {
            RootContent(root)
        }
    }
}
