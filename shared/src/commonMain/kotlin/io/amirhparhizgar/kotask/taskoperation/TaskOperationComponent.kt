package io.amirhparhizgar.kotask.taskoperation

import io.amirhparhizgar.kotask.list.Task
import io.amirhparhizgar.kotask.list.TaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

interface TaskOperationComponent {
    val task: Task

    fun setDone(done: Boolean): Job
}

data class DefaultTaskOperationComponent(
    override val task: Task,
    private val repository: TaskRepository,
) : TaskOperationComponent {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun setDone(done: Boolean): Job =
        coroutineScope.launch {
            repository.updateTaskIsDone(task.id, done)
        }
}
