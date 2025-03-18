package io.amirhparhizgar.kotask.list

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.doOnResume
import com.arkivanov.essenty.lifecycle.doOnStart
import com.arkivanov.essenty.lifecycle.doOnStop
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

interface TodoListComponent {
    val items: Value<List<Todo>>
}

class DefaultTodoListComponent(
    componentContext: ComponentContext,
    private val repo: FakeTodoRepository
) : TodoListComponent,
    ComponentContext by componentContext {
    private val _items: MutableValue<List<Todo>> = MutableValue(emptyList())
    override val items: Value<List<Todo>> = _items

    init {
        var job: Job? = null
        lifecycle.doOnStart {
            job = CoroutineScope(Dispatchers.Main).launch {
                repo.todos.collect {
                    _items.value = it
                }
            }
        }
        lifecycle.doOnStop { job?.cancel() }
    }
}
