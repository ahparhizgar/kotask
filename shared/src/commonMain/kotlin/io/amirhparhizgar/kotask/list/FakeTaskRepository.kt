package io.amirhparhizgar.kotask.list

import io.amirhparhizgar.kotask.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.LocalDate
import kotlin.random.Random

class FakeTaskRepository(
    initialTasks: List<Task> = listOf(
        Task(id = "1", title = "Buy milk", isDone = false, isImportant = false),
        Task(id = "2", title = "Walk the dog", isDone = false, isImportant = true),
    ),
) : TaskRepository {
    private val _tasks =
        MutableStateFlow(initialTasks)

    override val taskStream: Flow<List<Task>> = _tasks.asStateFlow()

    override suspend fun addTask(
        title: String,
        dueDate: LocalDate?,
    ): String {
        val id = Random.nextInt().toString()
        _tasks.value += Task(
            id = id,
            title = title,
            isDone = false,
            dueDate = dueDate,
            isImportant = false,
        )
        return id
    }

    suspend fun addFakeTask(title: String = "Fake Task") {
        addTask(title = title, dueDate = null)
    }

    override suspend fun updateDoneStatus(
        id: String,
        isDone: Boolean,
    ) {
        val updatedList =
            _tasks.value.mapIndexed { i, t ->
                if (t.id == id) t.copy(isDone = isDone) else t
            }
        _tasks.value = updatedList
    }

    override suspend fun get(id: String): Task = _tasks.value.first { it.id == id }
}
