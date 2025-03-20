package com.example.myapplication.desktop

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.example.myapplication.root.RootContent
import io.amirhparhizgar.kotask.list.DefaultTaskListComponent
import io.amirhparhizgar.kotask.list.FakeTodoRepository

fun main() {
    val lifecycle = LifecycleRegistry()

    val root =
        runOnUiThread {
            DefaultTaskListComponent(
                componentContext = DefaultComponentContext(lifecycle = lifecycle),
                repo = FakeTodoRepository(),
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
