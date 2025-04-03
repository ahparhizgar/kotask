package io.amirhparhizgar.kotask

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MultiPaneTasksContent(
    component: MultiPaneTasksComponent,
    modifier: Modifier = Modifier,
) {
    val listComponent = component.listComponent
    TaskListContent(listComponent, modifier, component)
}
