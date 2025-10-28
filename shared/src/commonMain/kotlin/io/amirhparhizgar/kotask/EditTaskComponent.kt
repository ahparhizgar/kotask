package io.amirhparhizgar.kotask

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.doOnStart
import com.arkivanov.essenty.lifecycle.doOnStop
import io.amirhparhizgar.kotask.list.TaskRepository
import io.amirhparhizgar.kotask.taskoperation.FakeTaskOperationComponent
import io.amirhparhizgar.kotask.taskoperation.TaskOperationComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

interface EditTaskComponent : TaskOperationComponent {
    val task: Value<LoadingTask>

    interface Factory {
        fun create(
            context: ComponentContext,
            id: String,
        ): EditTaskComponent
    }
}

sealed interface LoadingTask {
    data class NotLoaded(val id: String) : LoadingTask

    data class Loaded(val task: Task) : LoadingTask
}

class DefaultEditTaskComponent(
    id: String,
    context: ComponentContext,
    taskOperationComponent: TaskOperationComponent,
    private val repository: TaskRepository,
) : EditTaskComponent,
    TaskOperationComponent by taskOperationComponent,
    ComponentContext by context {
    private val _task = MutableValue<LoadingTask>(LoadingTask.NotLoaded(id))
    override val task: Value<LoadingTask> = _task
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    init {
        var job: Job? = null
        lifecycle.doOnStart {
            job = scope.launch {
                repository.taskStream.map { it.firstOrNull { it.id == id } }.collect { t ->
                    if (t != null) {
                        _task.update { LoadingTask.Loaded(t) }
                    }
                }
            }
        }
        lifecycle.doOnStop { job?.cancel() }
    }

    class Factory(
        private val repository: TaskRepository,
        private val taskOperationComponentFactory: TaskOperationComponent.Factory,
    ) : EditTaskComponent.Factory {
        override fun create(
            context: ComponentContext,
            id: String,
        ): EditTaskComponent =
            DefaultEditTaskComponent(
                id = id,
                context = context,
                repository = repository,
                taskOperationComponent = taskOperationComponentFactory.create(id),
            )
    }
}

class FakeEditTaskComponent(
    override val task: Value<LoadingTask> =
        MutableValue(LoadingTask.Loaded(FakeTaskFactory.create())),
    taskOperationComponent: TaskOperationComponent = FakeTaskOperationComponent(),
) : EditTaskComponent, TaskOperationComponent by taskOperationComponent
