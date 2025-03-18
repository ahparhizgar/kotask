package io.amirhparhizgar.kotask.list

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeTodoRepository {
    val todos: Flow<List<Todo>> =
        flowOf(
            listOf(
                Todo(title = "Buy milk", isDone = false),
                Todo(title = "Walk the dog", isDone = false)
            )
        )
}
