package io.amirhparhizgar.kotask.taskoperation

import io.amirhparhizgar.kotask.list.TaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

interface TaskOperationComponent {
    fun setTitle(title: String): Job

    fun setDone(done: Boolean): Job

    fun setImportance(isImportant: Boolean): Job
}

data class DefaultTaskOperationComponent(
    private val taskId: String,
    private val repository: TaskRepository,
) : TaskOperationComponent {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun setTitle(title: String): Job =
        coroutineScope.launch {
            repository.updateTitle(taskId, title)
        }

    override fun setDone(done: Boolean): Job =
        coroutineScope.launch {
            repository.updateDoneStatus(taskId, done)
        }

    override fun setImportance(isImportant: Boolean): Job =
        coroutineScope.launch {
            repository.setImportant(taskId, isImportant)
        }
}

class FakeTaskOperationComponent : TaskOperationComponent {
    override fun setTitle(title: String): Job = Job()

    override fun setDone(done: Boolean): Job = Job()

    override fun setImportance(isImportant: Boolean): Job = Job()
}
