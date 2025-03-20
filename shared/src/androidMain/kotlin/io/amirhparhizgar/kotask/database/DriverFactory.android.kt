package io.amirhparhizgar.kotask.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import io.amirhparhizgar.ToDoDatabase

actual class DriverFactory(
    private val context: Context,
) {
    actual fun createDriver(): SqlDriver =
        AndroidSqliteDriver(
            schema = ToDoDatabase.Schema,
            context = context,
            name = "to-do.db",
        )
}
