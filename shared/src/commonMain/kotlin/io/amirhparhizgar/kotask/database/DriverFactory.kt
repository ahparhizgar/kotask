package io.amirhparhizgar.kotask.database

import app.cash.sqldelight.db.SqlDriver
import io.amirhparhizgar.AppDatabase

interface DriverFactory {
    suspend fun createDriver(): SqlDriver
}

suspend fun createDatabase(driverFactory: DriverFactory): AppDatabase {
    val database = AppDatabase(driverFactory.createDriver())
    return database
}
