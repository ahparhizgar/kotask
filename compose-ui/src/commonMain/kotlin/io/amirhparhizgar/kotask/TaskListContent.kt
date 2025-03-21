package io.amirhparhizgar.kotask

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@Composable
fun TaskList(
    component: AllTasksComponent,
    modifier: Modifier = Modifier,
) {
    val tasks by component.listComponent.items.subscribeAsState()
    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            AddTaskContent(component.addComponent)
        }
        items(tasks) {
            Task(component = it)
        }
    }
}
