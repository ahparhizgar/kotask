package io.amirhparhizgar.kotask

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import io.amirhparhizgar.kotask.list.TaskListComponent

@Composable
fun TaskListContent(
    listComponent: TaskListComponent,
    modifier: Modifier,
    component: MultiPaneTasksComponent,
) {
    val tasks by listComponent.items.subscribeAsState()
    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            AddTaskContent(component.addComponent)
        }
        items(tasks) {
            Task(component = it)
        }
    }
}
