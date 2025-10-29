package io.amirhparhizgar.kotask.list

import io.amirhparhizgar.kotask.AddTaskComponent
import io.amirhparhizgar.kotask.DefaultAddTaskComponent
import io.amirhparhizgar.kotask.DefaultEditTaskComponent
import io.amirhparhizgar.kotask.DefaultMultiPaneTasksComponent
import io.amirhparhizgar.kotask.DefaultTaskItemComponent
import io.amirhparhizgar.kotask.EditTaskComponent
import io.amirhparhizgar.kotask.MultiPaneTasksComponent
import io.amirhparhizgar.kotask.TaskItemComponent
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val TaskListModule =
    module {
        single<TaskRepository> {
            // TODO use injection for dispatchers.io
            DatabaseTaskRepository(databaseFactory = get(), dispatcher = Dispatchers.IO)
        }

        factoryOf(DefaultTaskListComponent::Factory)
            .bind<TaskListComponent.Factory>()

        factoryOf(DefaultTaskItemComponent::Factory)
            .bind<TaskItemComponent.Factory>()

        factoryOf(DefaultAddTaskComponent::Factory)
            .bind<AddTaskComponent.Factory>()

        factoryOf(DefaultEditTaskComponent::Factory)
            .bind<EditTaskComponent.Factory>()

        factoryOf(DefaultMultiPaneTasksComponent::Factory)
            .bind<MultiPaneTasksComponent.Factory>()
    }
