package io.amirhparhizgar.kotask

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import io.amirhparhizgar.kotask.list.Task
import io.amirhparhizgar.kotask.list.TaskRepository
import io.amirhparhizgar.kotask.list.componentCoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

interface AddTaskComponent {
    val state: MutableValue<State>

    fun onTitleChange(newTitle: String)

    fun onAddClick(): Job

    data class State(
        val title: String = "",
        val addedTask: Task? = null,
    )
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

    override fun onAddClick() =
        coroutineScope.launch {
            val id = repository.addTask(state.value.title)
            val task = repository.get(id)
            state.update {
                it.copy(
                    addedTask = task,
                    title = "",
                )
            }
        }
}
