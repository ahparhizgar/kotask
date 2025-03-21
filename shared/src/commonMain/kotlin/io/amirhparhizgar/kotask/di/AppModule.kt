package io.amirhparhizgar.kotask.di

import io.amirhparhizgar.kotask.database.DatabaseModule
import org.koin.dsl.module

val AppModule = module {
    includes(DatabaseModule)
}
