package io.amirhparhizgar.kotask.database

import app.cash.sqldelight.db.SqlDriver
import io.amirhparhizgar.ToDoDatabase

expect class DriverFactory {
    fun createDriver(): SqlDriver
}

fun createDatabase(driverFactory: DriverFactory): ToDoDatabase {
    val driver = driverFactory.createDriver()
    val database = ToDoDatabase(driver)
    return database
}
