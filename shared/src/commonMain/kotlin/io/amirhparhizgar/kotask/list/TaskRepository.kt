package io.amirhparhizgar.kotask.list

import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    val tasks: Flow<List<Task>>

    suspend fun addTask(title: String): String

    suspend fun updateTaskIsDone(
        id: String,
        isDone: Boolean,
    )
}
