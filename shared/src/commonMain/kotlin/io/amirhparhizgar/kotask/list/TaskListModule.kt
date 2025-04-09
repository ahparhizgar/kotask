package io.amirhparhizgar.kotask.list

import com.arkivanov.decompose.ComponentContext
import io.amirhparhizgar.kotask.AddTaskComponent
import io.amirhparhizgar.kotask.DefaultAddTaskComponent
import io.amirhparhizgar.kotask.DefaultEditTaskComponent
import io.amirhparhizgar.kotask.DefaultMultiPaneTasksComponent
import io.amirhparhizgar.kotask.DefaultTaskItemComponent
import io.amirhparhizgar.kotask.EditTaskComponent
import io.amirhparhizgar.kotask.MultiPaneTasksComponent
import io.amirhparhizgar.kotask.Task
import io.amirhparhizgar.kotask.TaskItemComponent
import io.amirhparhizgar.kotask.taskoperation.DefaultTaskOperationComponent
import io.amirhparhizgar.kotask.taskoperation.TaskOperationComponent
import kotlinx.coroutines.Dispatchers
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val TaskListModule =
    module {
        single<TaskRepository> {
            // TODO use injection for dispatchers.io
            DatabaseTaskRepository(databaseFactory = get(), dispatcher = Dispatchers.IO)
        }

        factory<TaskListComponent> { (context: ComponentContext, onEditRequested: (id: String) -> Unit) ->
            DefaultTaskListComponent(
                componentContext = context,
                repo = get(),
                taskItemFactory = { task: Task, onEdit: () -> Unit ->
                    get<TaskItemComponent> { parametersOf(task, onEdit) }
                },
                onEditRequested = onEditRequested,
            )
        }

        factory<TaskItemComponent> { (task: Task, onEdit: () -> Unit) ->
            DefaultTaskItemComponent(
                task = task,
                taskOperationComponent = get { parametersOf(task.id) },
                onEdit = onEdit,
            )
        }
        factory<TaskOperationComponent> { (taskId: String) ->
            DefaultTaskOperationComponent(
                taskId = taskId,
                repository = get(),
            )
        }

        factory<AddTaskComponent> { (context: ComponentContext) ->
            DefaultAddTaskComponent(
                context = context,
                repository = get(),
            )
        }

        factory<EditTaskComponent> { (ctx: ComponentContext, id: String) ->
            DefaultEditTaskComponent(
                id = id,
                context = ctx,
                repository = get(),
                taskOperationComponent = DefaultTaskOperationComponent(
                    taskId = id,
                    repository = get(),
                ),
            )
        }

        factory<MultiPaneTasksComponent> { (context: ComponentContext) ->
            DefaultMultiPaneTasksComponent(
                componentContext = context,
                listComponentFactory = { c: ComponentContext, onEdit: (id: String) -> Unit ->
                    get<TaskListComponent> { parametersOf(c, onEdit) }
                },
                editComponentFactory = { c, id: String ->
                    get<EditTaskComponent> { parametersOf(c, id) }
                },
                addComponentFactory = { c: ComponentContext ->
                    get<AddTaskComponent> { parametersOf(c) }
                },
            )
        }
    }
