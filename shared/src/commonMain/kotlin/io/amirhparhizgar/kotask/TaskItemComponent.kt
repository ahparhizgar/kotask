package io.amirhparhizgar.kotask

import io.amirhparhizgar.kotask.taskoperation.FakeTaskOperationComponent
import io.amirhparhizgar.kotask.taskoperation.TaskOperationComponent

interface TaskItemComponent : TaskOperationComponent {
    val task: Task

    fun onEditClick()

    interface Factory {
        fun create(
            task: Task,
            onEdit: () -> Unit,
        ): TaskItemComponent
    }
}

class DefaultTaskItemComponent(
    override val task: Task,
    taskOperationComponent: TaskOperationComponent,
    private val onEdit: () -> Unit,
) : TaskItemComponent, TaskOperationComponent by taskOperationComponent {
    override fun onEditClick() = onEdit.invoke()

    class Factory(
        private val taskOperationComponentFactory: TaskOperationComponent.Factory,
    ) : TaskItemComponent.Factory {
        override fun create(
            task: Task,
            onEdit: () -> Unit,
        ): TaskItemComponent =
            DefaultTaskItemComponent(
                task = task,
                taskOperationComponent = taskOperationComponentFactory.create(task.id),
                onEdit = onEdit,
            )
    }
}

class FakeTaskItemComponent(
    override val task: Task = FakeTaskFactory.create(),
    private val taskOperationComponent: TaskOperationComponent = FakeTaskOperationComponent(),
) : TaskItemComponent, TaskOperationComponent by taskOperationComponent {
    override fun onEditClick() {}

    class Factory : TaskItemComponent.Factory {
        override fun create(
            task: Task,
            onEdit: () -> Unit,
        ): TaskItemComponent = FakeTaskItemComponent(task = task)
    }
}
