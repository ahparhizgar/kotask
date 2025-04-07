package io.amirhparhizgar.kotask

import io.amirhparhizgar.kotask.taskoperation.FakeTaskOperationComponent
import io.amirhparhizgar.kotask.taskoperation.TaskOperationComponent

interface TaskItemComponent : TaskOperationComponent {
    val task: Task
}

class DefaultTaskItemComponent(
    override val task: Task,
    taskOperationComponent: TaskOperationComponent,
) : TaskItemComponent, TaskOperationComponent by taskOperationComponent

class FakeTaskItemComponent(
    override val task: Task = FakeTaskFactory.create(),
    private val taskOperationComponent: TaskOperationComponent = FakeTaskOperationComponent(),
) : TaskItemComponent, TaskOperationComponent by taskOperationComponent
