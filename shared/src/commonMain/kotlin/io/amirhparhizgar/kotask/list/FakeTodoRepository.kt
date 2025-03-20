package io.amirhparhizgar.kotask.list

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeTodoRepository {
    val todos: Flow<List<Task>> =
        flowOf(
            listOf(
                Task(title = "Buy milk", isDone = false),
                Task(title = "Walk the dog", isDone = false)
            )
        )
}
