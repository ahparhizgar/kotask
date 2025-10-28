package io.amirhparhizgar.kotask

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import io.amirhparhizgar.kotask.list.TaskRepository
import io.amirhparhizgar.kotask.list.componentCoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

interface AddTaskComponent {
    val state: MutableValue<State>

    fun onTitleChange(newTitle: String)

    fun onDueDateChange(newDueDate: LocalDate?)

    fun onAddClick(): Job

    data class State(
        val title: String = "",
        val dueDate: LocalDate? = null,
        val addedTask: Task? = null,
    )

    interface Factory {
        fun create(context: ComponentContext): AddTaskComponent
    }
}

class DefaultAddTaskComponent(context: ComponentContext, private val repository: TaskRepository) :
    ComponentContext by context,
    AddTaskComponent {
    private val coroutineScope = componentCoroutineScope()
    override val state = MutableValue(AddTaskComponent.State())

    override fun onTitleChange(newTitle: String) {
        state.update {
            it.copy(title = newTitle)
        }
    }

    override fun onDueDateChange(newDueDate: LocalDate?) {
        state.update {
            it.copy(dueDate = newDueDate)
        }
    }

    override fun onAddClick() =
        coroutineScope.launch {
            val id = repository.addTask(state.value.title, state.value.dueDate)
            val task = repository.get(id)
            state.update {
                it.copy(
                    addedTask = task,
                    title = "",
                    dueDate = null,
                )
            }
        }

    class Factory(private val repository: TaskRepository) : AddTaskComponent.Factory {
        override fun create(context: ComponentContext): AddTaskComponent =
            DefaultAddTaskComponent(
                context = context,
                repository = repository,
            )
    }
}
