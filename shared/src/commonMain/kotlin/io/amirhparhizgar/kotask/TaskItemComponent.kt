package io.amirhparhizgar.kotask

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import io.amirhparhizgar.kotask.list.TaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

interface TaskItemComponent {
    val task: Task

    fun setTitle(title: String): Job

    fun setDone(done: Boolean): Job

    fun setImportance(isImportant: Boolean): Job

    fun onEditClick()

    interface Factory {
        fun create(
            context: ComponentContext,
            task: Task,
            onEdit: () -> Unit,
        ): TaskItemComponent
    }
}

class DefaultTaskItemComponent(
    context: ComponentContext,
    override val task: Task,
    private val repository: TaskRepository,
    private val onEdit: () -> Unit,
) : TaskItemComponent, ComponentContext by context {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    init {
        lifecycle.doOnDestroy { coroutineScope.cancel("Component Destroyed") }
    }

    override fun setTitle(title: String): Job =
        coroutineScope.launch {
            repository.updateTitle(task.id, title)
        }

    override fun setDone(done: Boolean): Job =
        coroutineScope.launch {
            repository.updateDoneStatus(task.id, done)
        }

    override fun setImportance(isImportant: Boolean): Job =
        coroutineScope.launch {
            repository.setImportant(task.id, isImportant)
        }

    override fun onEditClick() = onEdit.invoke()

    class Factory(
        private val taskRepository: TaskRepository,
    ) : TaskItemComponent.Factory {
        override fun create(
            context: ComponentContext,
            task: Task,
            onEdit: () -> Unit,
        ): TaskItemComponent =
            DefaultTaskItemComponent(
                context = context,
                task = task,
                repository = taskRepository,
                onEdit = onEdit,
            )
    }
}

class TaskItemConfiguration(
    val task: Task,
)

class FakeTaskItemComponent(
    override val task: Task = FakeTaskFactory.create(),
) : TaskItemComponent {
    override fun setTitle(title: String): Job {
        error("Fake component")
    }

    override fun setDone(done: Boolean): Job {
        error("Fake component")
    }

    override fun setImportance(isImportant: Boolean): Job {
        error("Fake component")
    }

    override fun onEditClick() {}

    class Factory : TaskItemComponent.Factory {
        override fun create(
            context: ComponentContext,
            task: Task,
            onEdit: () -> Unit,
        ): TaskItemComponent = FakeTaskItemComponent(task = task)
    }
}
