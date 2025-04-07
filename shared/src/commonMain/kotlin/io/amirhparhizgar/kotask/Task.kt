package io.amirhparhizgar.kotask

import kotlinx.datetime.LocalDate

data class Task(
    val id: String,
    val title: String,
    val isDone: Boolean,
    val dueDate: LocalDate? = null,
    val isImportant: Boolean,
)

object FakeTaskFactory {
    fun create() =
        Task(
            id = "1",
            title = "Task 1",
            isDone = false,
            isImportant = false,
        )
}
