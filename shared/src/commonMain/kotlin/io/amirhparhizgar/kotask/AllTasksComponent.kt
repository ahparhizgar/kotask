package io.amirhparhizgar.kotask

import com.arkivanov.decompose.ComponentContext
import io.amirhparhizgar.kotask.list.DefaultTaskListComponent

interface AllTasksComponent {
    val listComponent: DefaultTaskListComponent
    val addComponent: DefaultAddTaskComponent
}

class DefaultAllTasksComponent(
    componentContext: ComponentContext,
    listComponentFactory: (context: ComponentContext) -> DefaultTaskListComponent,
    addComponentFactory: (context: ComponentContext) -> DefaultAddTaskComponent,
) : ComponentContext by componentContext, AllTasksComponent {
    override val listComponent = listComponentFactory(componentContext)
    override val addComponent = addComponentFactory(componentContext)
}
