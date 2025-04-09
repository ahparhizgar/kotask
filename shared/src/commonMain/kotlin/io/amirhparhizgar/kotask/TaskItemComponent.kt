package io.amirhparhizgar.kotask

import io.amirhparhizgar.kotask.taskoperation.FakeTaskOperationComponent
import io.amirhparhizgar.kotask.taskoperation.TaskOperationComponent

interface TaskItemComponent : TaskOperationComponent {
    val task: Task

    fun onEditClick()
}

class DefaultTaskItemComponent(
    override val task: Task,
    taskOperationComponent: TaskOperationComponent,
    private val onEdit: () -> Unit,
) : TaskItemComponent, TaskOperationComponent by taskOperationComponent {
    override fun onEditClick() = onEdit.invoke()
}

class FakeTaskItemComponent(
    override val task: Task = FakeTaskFactory.create(),
    private val taskOperationComponent: TaskOperationComponent = FakeTaskOperationComponent(),
) : TaskItemComponent, TaskOperationComponent by taskOperationComponent {
    override fun onEditClick() {}
}
