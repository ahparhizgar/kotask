package io.amirhparhizgar.kotask.root

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.amirhparhizgar.kotask.list.Task
import io.amirhparhizgar.kotask.list.TaskListComponent

@Composable
fun RootContent(
    component: TaskListComponent,
    modifier: Modifier = Modifier,
) {
    MaterialTheme {
        Surface(modifier = modifier.fillMaxSize().windowInsetsPadding(WindowInsets.systemBars)) {
            val tasks by component.items.subscribeAsState()
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(tasks) {
                    Task(it)
                }
            }
        }
    }
}

@Composable
fun Task(
    task: Task,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        Text(task.title)
    }
}
