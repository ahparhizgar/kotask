package io.amirhparhizgar.kotask.list

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.essenty.lifecycle.doOnStart
import com.arkivanov.essenty.lifecycle.doOnStop
import io.amirhparhizgar.kotask.FakeTaskItemComponent
import io.amirhparhizgar.kotask.Task
import io.amirhparhizgar.kotask.TaskItemComponent
import io.amirhparhizgar.kotask.taskoperation.FakeTaskOperationComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

interface TaskListComponent {
    val items: Value<List<TaskItemComponent>>

    interface Factory {
        fun create(
            context: ComponentContext,
            onEditRequested: (id: String) -> Unit
        ): TaskListComponent
    }
}

class DefaultTaskListComponent(
    componentContext: ComponentContext,
    private val repo: TaskRepository,
    private val taskItemFactory: TaskItemComponent.Factory,
    private val onEditRequested: (id: String) -> Unit,
) : TaskListComponent,
    ComponentContext by componentContext {
    private val coroutineScope = componentCoroutineScope()
    private val _items: MutableValue<List<TaskItemComponent>> = MutableValue(emptyList())
    override val items: Value<List<TaskItemComponent>> = _items

    init {
        var job: Job? = null
        lifecycle.doOnStart {
            job =
                coroutineScope.launch {
                    repo.taskStream.collect {
                        _items.value =
                            it.map { t -> taskItemFactory.create(t, { onEditRequested(t.id) }) }
                    }
                }
        }
        lifecycle.doOnStop { job?.cancel() }
    }

    class Factory(
        private val repo: TaskRepository,
        private val taskItemFactory: TaskItemComponent.Factory,
    ) : TaskListComponent.Factory {
        override fun create(
            context: ComponentContext,
            onEditRequested: (id: String) -> Unit
        ): TaskListComponent =
            DefaultTaskListComponent(
                componentContext = context,
                repo = repo,
                taskItemFactory = taskItemFactory,
                onEditRequested = onEditRequested,
            )
    }
}

fun ComponentContext.componentCoroutineScope(): CoroutineScope {
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    lifecycle.doOnDestroy {
        scope.cancel()
    }
    return scope
}

class FakeTaskListComponent(
    override val items: MutableValue<List<TaskItemComponent>>,
) : TaskListComponent {
    constructor(
        items: List<Task>,
    ) : this(MutableValue(items.map { FakeTaskItemComponent(it, FakeTaskOperationComponent()) }))
}
