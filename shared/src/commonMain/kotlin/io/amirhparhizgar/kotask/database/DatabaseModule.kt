package io.amirhparhizgar.kotask.database

import org.koin.core.module.Module
import org.koin.dsl.module

val DatabaseModule =
    module {
        includes(DriverFactoryModule)

        single {
            DatabaseFactory(get<DriverFactory>())
        }
    }

expect val DriverFactoryModule: Module
