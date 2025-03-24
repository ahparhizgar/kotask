package io.amirhparhizgar.kotask.list

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface TaskRepository {
    val tasks: Flow<List<Task>>

    suspend fun addTask(
        title: String,
        dueDate: LocalDate? = null
    ): String

    suspend fun updateDoneStatus(
        id: String,
        isDone: Boolean,
    )

    suspend fun get(id: String): Task
}
