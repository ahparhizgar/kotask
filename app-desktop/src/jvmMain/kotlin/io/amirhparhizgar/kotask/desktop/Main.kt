package io.amirhparhizgar.kotask.desktop

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import io.amirhparhizgar.kotask.di.AppModule
import io.amirhparhizgar.kotask.list.DefaultTaskListComponent
import io.amirhparhizgar.kotask.list.TaskRepository
import io.amirhparhizgar.kotask.root.RootContent
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin

fun main() {
    startKoin {
        modules(AppModule)
    }

    val lifecycle = LifecycleRegistry()
    val taskRepository = GlobalContext.get().get<TaskRepository>()

    val root =
        runOnUiThread {
            DefaultTaskListComponent(
                componentContext = DefaultComponentContext(lifecycle = lifecycle),
                repo = taskRepository,
            )
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
