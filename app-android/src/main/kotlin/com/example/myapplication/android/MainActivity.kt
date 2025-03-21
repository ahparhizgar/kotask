package com.example.myapplication.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import io.amirhparhizgar.kotask.root.RootContent
import io.amirhparhizgar.kotask.list.DefaultTaskListComponent
import io.amirhparhizgar.kotask.list.TaskRepository
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val taskRepository: TaskRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root =
            DefaultTaskListComponent(
                componentContext = defaultComponentContext(),
                repo = taskRepository,
            )

        setContent {
            RootContent(component = root)
        }
    }
}
