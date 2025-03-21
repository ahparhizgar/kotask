package io.amirhparhizgar.kotask.list

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeTaskRepository : TaskRepository {
    private val _tasks =
        MutableStateFlow(
            listOf(
                Task(title = "Buy milk", isDone = false),
                Task(title = "Walk the dog", isDone = false),
            ),
        )

    override val todos: Flow<List<Task>> = _tasks.asStateFlow()

    override suspend fun addTask(title: String) {
        _tasks.value += Task(title = title, isDone = false)
    }

    override suspend fun updateTaskIsDone(
        id: Long,
        isDone: Boolean,
    ) {
        // In this fake implementation, we'll just assume the id is the index in the list
        val index = id.toInt()
        if (index < _tasks.value.size) {
            val updatedList = _tasks.value.toMutableList()
            val task = updatedList[index]
            updatedList[index] = task.copy(isDone = isDone)
            _tasks.value = updatedList
        }
    }
}
