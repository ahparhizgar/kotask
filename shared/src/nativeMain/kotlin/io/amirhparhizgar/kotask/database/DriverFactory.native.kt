package io.amirhparhizgar.kotask.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import io.amirhparhizgar.ToDoDatabase

actual class DriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(ToDoDatabase.Schema, "test.db")
    }
}