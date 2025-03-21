package io.amirhparhizgar.kotask.list

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import io.amirhparhizgar.kotask.database.DatabaseFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class DatabaseTaskRepository(
    private val databaseFactory: DatabaseFactory,
) : TaskRepository {
    override val todos: Flow<List<Task>>
        get() =
            flow {
                emitAll(
                    databaseFactory
                        .getDatabase()
                        .toDoDatabaseQueries
                        .selectAll()
                        .asFlow()
                        .mapToList(currentCoroutineContext())
                        .map { entities ->
                            entities.map { entity ->
                                Task(
                                    title = entity.text,
                                    isDone = entity.isDone != 0L,
                                )
                            }
                        },
                )
            }

    override suspend fun addTask(title: String) =
        withContext(Dispatchers.Default) {
            databaseFactory.getDatabase().toDoDatabaseQueries.add(title)
        }

    override suspend fun updateTaskIsDone(
        id: Long,
        isDone: Boolean,
    ) = withContext(Dispatchers.Default) {
        databaseFactory.getDatabase().toDoDatabaseQueries.updateIsDone(
            isDone = if (isDone) 1 else 0,
            id = id,
        )
    }
}
