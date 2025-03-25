package io.amirhparhizgar.kotask.list

import kotlinx.datetime.LocalDate

data class Task(
    val id: String,
    val title: String,
    val isDone: Boolean,
    val dueDate: LocalDate? = null,
)

object FakeTaskFactory {
    fun create() =
        Task(
            id = "1",
            title = "Task 1",
            isDone = false,
        )
}
