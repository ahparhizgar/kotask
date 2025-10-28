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

        factory<TaskListComponent.Factory> {
            DefaultTaskListComponent.Factory(
                repo = get(),
                taskItemFactory = get(),
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

        factory<EditTaskComponent.Factory> {
            DefaultEditTaskComponent.Factory(
                repository = get(),
                taskOperationComponentFactory = get()
            )
        }

        factory<MultiPaneTasksComponent> { (context: ComponentContext) ->
            DefaultMultiPaneTasksComponent(
                componentContext = context,
                listComponentFactory = get(),
                editComponentFactory = get(),
                addComponentFactory = get(),
            )
        }
    }
