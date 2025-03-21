package io.amirhparhizgar.kotask.list

import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    val todos: Flow<List<Task>>

    suspend fun addTask(title: String)

    suspend fun updateTaskIsDone(
        id: Long,
        isDone: Boolean,
    )
}
