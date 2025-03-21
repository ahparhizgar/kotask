package io.amirhparhizgar.kotask

import com.arkivanov.decompose.ComponentContext
import io.amirhparhizgar.kotask.list.TaskListComponent

interface AllTasksComponent {
    val listComponent: TaskListComponent
    val addComponent: AddTaskComponent
}

class DefaultAllTasksComponent(
    componentContext: ComponentContext,
    listComponentFactory: (context: ComponentContext) -> TaskListComponent,
    addComponentFactory: (context: ComponentContext) -> AddTaskComponent,
) : ComponentContext by componentContext, AllTasksComponent {
    override val listComponent = listComponentFactory(componentContext)
    override val addComponent = addComponentFactory(componentContext)
}
