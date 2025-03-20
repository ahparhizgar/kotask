package io.amirhparhizgar.kotask.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import io.amirhparhizgar.AppDatabase
import java.io.File

class JvmDriverFactory : DriverFactory {
    override suspend fun createDriver(): SqlDriver {
        val databasePath = File("AppDatabase.db")
        return JdbcSqliteDriver(url = "jdbc:sqlite:${databasePath.absolutePath}").also { driver ->
            AppDatabase.Schema.create(driver).await()
        }
    }
}

class JvmInMemoryDriverFactory : DriverFactory {
    override suspend fun createDriver(): SqlDriver {
        val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        AppDatabase.Schema.create(driver).await()
        return driver
    }
}
