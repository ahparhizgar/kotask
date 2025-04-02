package io.amirhparhizgar.kotask

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.doOnStart
import com.arkivanov.essenty.lifecycle.doOnStop
import io.amirhparhizgar.kotask.list.TaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

interface EditTaskComponent {
    val task: Value<LoadingTask>

    fun setTitle(title: String)

    fun setNote(note: String)

    fun setDone(done: Boolean)
}

sealed interface LoadingTask {
    data class NotLoaded(val id: String) : LoadingTask

    data class Loaded(val task: Task) : LoadingTask
}

class DefaultEditTaskComponent(
    id: String,
    context: ComponentContext,
    private val repository: TaskRepository,
) : EditTaskComponent, ComponentContext by context {
    private val _task = MutableValue<LoadingTask>(LoadingTask.NotLoaded(id))
    override val task: Value<LoadingTask> = _task
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    init {
        var job: Job? = null
        lifecycle.doOnStart {
            job = scope.launch {
                repository.taskStream.map { it.firstOrNull { it.id == id } }.collect { t ->
                    if (t != null) {
                        _task.update { LoadingTask.Loaded(t) }
                    }
                }
            }
        }
        lifecycle.doOnStop { job?.cancel() }
    }

    override fun setTitle(title: String) {}

    override fun setNote(note: String) {}

    override fun setDone(done: Boolean) {}
}

class FakeEditTaskComponent(
    override val task: Value<LoadingTask> =
        MutableValue(LoadingTask.Loaded(FakeTaskFactory.create())),
) : EditTaskComponent {
    override fun setTitle(title: String) {}

    override fun setNote(note: String) {}

    override fun setDone(done: Boolean) {}
}
