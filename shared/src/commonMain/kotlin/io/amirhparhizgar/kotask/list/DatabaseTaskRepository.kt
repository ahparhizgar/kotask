package io.amirhparhizgar.kotask.list

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import io.amirhparhizgar.kotask.Task
import io.amirhparhizgar.kotask.database.DatabaseFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate

class DatabaseTaskRepository(
    private val databaseFactory: DatabaseFactory,
    private val dispatcher: CoroutineDispatcher,
) : TaskRepository {
    override val taskStream: Flow<List<Task>>
        get() =
            flow {
                emitAll(
                    databaseFactory
                        .getDatabase()
                        .taskDatabaseQueries
                        .selectAll()
                        .asFlow()
                        .mapToList(currentCoroutineContext())
                        .map { entities ->
                            entities.map { entity ->
                                Task(
                                    id = entity.id.toString(),
                                    title = entity.text,
                                    isDone = entity.isDone != 0L,
                                    dueDate = entity.dueDate?.let { LocalDate.fromEpochDays(it.toInt()) },
                                )
                            }
                        },
                )
            }

    override suspend fun addTask(
        title: String,
        dueDate: LocalDate?,
    ) = withContext(dispatcher) {
        with(databaseFactory.getDatabase()) {
            transactionWithResult {
                taskDatabaseQueries.add(text = title, dueDate = dueDate?.toEpochDays()?.toLong())
                taskDatabaseQueries.lastId().executeAsOne().toString()
            }
        }
    }

    override suspend fun updateDoneStatus(
        id: String,
        isDone: Boolean,
    ) = withContext(dispatcher) {
        databaseFactory.getDatabase().taskDatabaseQueries.updateIsDone(
            isDone = if (isDone) 1 else 0,
            id = id.toLong(),
        )
    }

    override suspend fun get(id: String): Task =
        withContext(dispatcher) {
            databaseFactory
                .getDatabase()
                .taskDatabaseQueries
                .select(id.toLong())
                .executeAsOne()
                .let { entity ->
                    Task(
                        id = entity.id.toString(),
                        title = entity.text,
                        isDone = entity.isDone != 0L,
                        dueDate = entity.dueDate?.let { LocalDate.fromEpochDays(it.toInt()) },
                    )
                }
        }
}
