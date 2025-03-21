package io.amirhparhizgar.kotask.list

import com.arkivanov.decompose.ComponentContext
import io.amirhparhizgar.kotask.taskoperation.DefaultTaskOperationComponent
import io.amirhparhizgar.kotask.taskoperation.TaskOperationComponent
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val TaskListModule =
    module {
        single<TaskRepository> {
            DatabaseTaskRepository(databaseFactory = get())
        }

        factory<TaskListComponent> { (context: ComponentContext) ->
            DefaultTaskListComponent(
                componentContext = context,
                repo = get(),
                taskOperationFactory = { task: Task ->
                    get<TaskOperationComponent> { parametersOf(task) }
                },
            )
        }

        factory<TaskOperationComponent> { (task: Task) ->
            DefaultTaskOperationComponent(
                task = task,
                repository = get(),
            )
        }
    }
