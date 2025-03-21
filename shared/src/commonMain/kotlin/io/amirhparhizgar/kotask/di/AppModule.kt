package io.amirhparhizgar.kotask.di

import io.amirhparhizgar.kotask.database.DatabaseModule
import io.amirhparhizgar.kotask.list.TaskListModule
import org.koin.dsl.module

val AppModule = module {
    includes(DatabaseModule)
    includes(TaskListModule)
}
