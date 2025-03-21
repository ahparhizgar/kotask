package io.amirhparhizgar.kotask.database

import io.amirhparhizgar.AppDatabase

class DatabaseFactory(private val driverFactory: DriverFactory) {
    private var database: AppDatabase? = null

    suspend fun getDatabase(): AppDatabase = database ?: createDatabase(driverFactory).also { database = it }
}
