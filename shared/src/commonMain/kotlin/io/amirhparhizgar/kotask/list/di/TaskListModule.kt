package io.amirhparhizgar.kotask.list.di

import io.amirhparhizgar.kotask.list.DatabaseTaskRepository
import io.amirhparhizgar.kotask.list.TaskRepository
import org.koin.dsl.module

val TaskListModule =
    module {
        single<TaskRepository> {
            DatabaseTaskRepository(databaseFactory = get())
        }
    }
