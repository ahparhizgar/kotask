package io.amirhparhizgar.kotask.database

import org.koin.core.module.Module
import org.koin.dsl.module

actual val DriverFactoryModule: Module =
    module {
        single<DriverFactory> {
            JvmDriverFactory()
        }
    }
