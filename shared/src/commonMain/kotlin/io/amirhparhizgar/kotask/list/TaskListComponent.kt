package io.amirhparhizgar.kotask.list

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.items.Items
import com.arkivanov.decompose.router.items.ItemsNavigation
import com.arkivanov.decompose.router.items.LazyChildItems
import com.arkivanov.decompose.router.items.childItems
import com.arkivanov.decompose.router.items.setItems
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.essenty.lifecycle.doOnStart
import com.arkivanov.essenty.lifecycle.doOnStop
import io.amirhparhizgar.kotask.Task
import io.amirhparhizgar.kotask.TaskItemComponent
import io.amirhparhizgar.kotask.TaskItemConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

@OptIn(ExperimentalDecomposeApi::class)
interface TaskListComponent {
    val items: LazyChildItems<TaskItemConfiguration, TaskItemComponent>

    interface Factory {
        fun create(
            context: ComponentContext,
            onEditRequested: (id: String) -> Unit
        ): TaskListComponent
    }
}

@OptIn(ExperimentalDecomposeApi::class)
class DefaultTaskListComponent(
    componentContext: ComponentContext,
    private val repo: TaskRepository,
    private val taskItemFactory: TaskItemComponent.Factory,
    private val onEditRequested: (id: String) -> Unit,
) : TaskListComponent,
    ComponentContext by componentContext {
    private val coroutineScope = componentCoroutineScope()

    private val navigation = ItemsNavigation<TaskItemConfiguration>()

    override val items: LazyChildItems<TaskItemConfiguration, TaskItemComponent> =
        childItems(
            source = navigation,
            serializer = null,
            initialItems = { Items(items = emptyList()) },
        ) { configuration, context ->
            taskItemFactory.create(
                context = DefaultComponentContext(LifecycleRegistry()),
                task = configuration.task,
                onEdit = { onEditRequested(configuration.task.id) }
            )
        }

    init {
        var job: Job? = null
        lifecycle.doOnStart {
            job =
                coroutineScope.launch {
                    repo.taskStream.collect { tasks ->
                        navigation.setItems { tasks.map { t -> TaskItemConfiguration(t) } }
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

@OptIn(ExperimentalDecomposeApi::class)
class FakeTaskListComponent(
    override val items: LazyChildItems<TaskItemConfiguration, TaskItemComponent>,
) : TaskListComponent {
    constructor(
        componentContext: ComponentContext,
        itemList: List<Task>,
        taskItemFactory: TaskItemComponent.Factory
    ) : this(
        items = componentContext.childItems(
            source = ItemsNavigation<TaskItemConfiguration>(),
            serializer = null,
            initialItems = { Items(items = itemList.map { TaskItemConfiguration(it) }) },
        ) { configuration: TaskItemConfiguration, context: ComponentContext ->
            taskItemFactory.create(
                context = DefaultComponentContext(LifecycleRegistry()),
                task = configuration.task,
                onEdit = { }
            )
        }
    )
}
