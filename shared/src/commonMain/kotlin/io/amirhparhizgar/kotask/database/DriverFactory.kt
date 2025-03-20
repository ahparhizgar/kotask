package io.amirhparhizgar.kotask.database

import app.cash.sqldelight.db.SqlDriver
import io.amirhparhizgar.AppDatabase

expect class DriverFactory {
    fun createDriver(): SqlDriver
}

fun createDatabase(driverFactory: DriverFactory): AppDatabase {
    val driver = driverFactory.createDriver()
    val database = AppDatabase(driver)
    return database
}
