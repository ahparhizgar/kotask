package io.amirhparhizgar.kotask.database

import android.content.Context
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import io.amirhparhizgar.AppDatabase

class AndroidDriverFactory(private val context: Context) : DriverFactory {
    override suspend fun createDriver(): SqlDriver =
        AndroidSqliteDriver(
            schema = AppDatabase.Schema.synchronous(),
            context = context,
            name = "AppDatabase.db",
        ).also { driver ->
            AppDatabase.Schema.create(driver).await()
        }
}

// I don't know it's really in memory
class AndroidInMemoryDriverFactory(private val context: Context) : DriverFactory {
    override suspend fun createDriver(): SqlDriver {
        val driver =
            AndroidSqliteDriver(
                schema = AppDatabase.Schema.synchronous(),
                context = context,
                name = null,
            )
        AppDatabase.Schema.create(driver).await()
        return driver
    }
}
