package io.amirhparhizgar.kotask

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.items.ChildItems
import io.amirhparhizgar.kotask.list.TaskListComponent

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun TaskListContent(
    component: TaskListComponent,
    addComponent: AddTaskComponent,
    modifier: Modifier = Modifier,
) {
    val tasks: ChildItems<TaskItemConfiguration, TaskItemComponent> =
        component.items.subscribeAsState().value
    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            AddTaskContent(addComponent)
        }
        items(items = tasks.items) {
            Task(component = component.items[it])
        }
    }
}
