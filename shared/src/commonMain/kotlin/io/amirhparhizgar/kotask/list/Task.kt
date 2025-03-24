package io.amirhparhizgar.kotask.list

import kotlinx.datetime.LocalDate

data class Task(
    val id: String,
    val title: String,
    val isDone: Boolean,
    val dueDate: LocalDate? = null,
)
