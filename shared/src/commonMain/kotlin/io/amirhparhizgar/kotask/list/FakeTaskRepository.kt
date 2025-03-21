package io.amirhparhizgar.kotask.list

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

class FakeTaskRepository : TaskRepository {
    private val _tasks =
        MutableStateFlow(
            listOf(
                Task(id = "1", title = "Buy milk", isDone = false),
                Task(id = "2", title = "Walk the dog", isDone = false),
            ),
        )

    override val tasks: Flow<List<Task>> = _tasks.asStateFlow()

    override suspend fun addTask(title: String): String {
        val id = Random.nextInt().toString()
        _tasks.value += Task(id = id, title = title, isDone = false)
        return id
    }

    override suspend fun updateTaskIsDone(
        id: String,
        isDone: Boolean,
    ) {
        val updatedList =
            _tasks.value.mapIndexed { i, t ->
                if (t.id == id) t.copy(isDone = isDone) else t
            }
        _tasks.value = updatedList
    }
}
