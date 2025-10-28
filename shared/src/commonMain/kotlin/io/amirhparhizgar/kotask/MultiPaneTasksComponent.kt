package io.amirhparhizgar.kotask

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.panels.ChildPanels
import com.arkivanov.decompose.router.panels.ChildPanelsMode
import com.arkivanov.decompose.router.panels.Panels
import com.arkivanov.decompose.router.panels.PanelsNavigation
import com.arkivanov.decompose.router.panels.childPanels
import com.arkivanov.decompose.router.panels.navigate
import com.arkivanov.decompose.value.Value
import io.amirhparhizgar.kotask.list.TaskListComponent

@OptIn(ExperimentalDecomposeApi::class)
interface MultiPaneTasksComponent {
    val panels: Value<ChildPanels<Unit, ViewSelector, Unit, TaskListComponent, EditTask, EditTaskComponent>>
    val addComponent: AddTaskComponent

    fun openDetails(task: Task)

    fun setMode(mode: ChildPanelsMode)

    interface Factory {
        fun create(
            componentContext: ComponentContext,
        ): MultiPaneTasksComponent
    }
}

data class EditTask(val taskId: String)

class ViewSelector

@OptIn(ExperimentalDecomposeApi::class)
class DefaultMultiPaneTasksComponent(
    componentContext: ComponentContext,
    private val listComponentFactory: TaskListComponent.Factory,
    private val editComponentFactory: EditTaskComponent.Factory,
    addComponentFactory: AddTaskComponent.Factory,
) : ComponentContext by componentContext, MultiPaneTasksComponent {
    private val navigation = PanelsNavigation<Unit, Unit, EditTask>()
    override val panels =
        childPanels(
            source = navigation,
            initialPanels = { Panels(main = Unit, details = Unit, extra = null) },
            serializers = null,
            handleBackButton = true,
            mainFactory = { _, ctx -> ViewSelector() },
            detailsFactory = { _, ctx -> listComponentFactory.create(ctx, ::navigateToEdit) },
            extraFactory = { cfg, ctx -> editComponentFactory.create(ctx, cfg.taskId) },
        )

    // todo move it to a new component containing list component
    override val addComponent = addComponentFactory.create(componentContext)

    override fun openDetails(task: Task) {
        navigation.navigate { state ->
            state.copy(extra = EditTask(task.id))
        }
    }

    override fun setMode(mode: ChildPanelsMode) {
        navigation.navigate { state ->
            state.copy(mode = mode)
        }
    }

    private fun navigateToEdit(taskId: String) {
        navigation.navigate { state ->
            state.copy(extra = EditTask(taskId))
        }
    }

    class Factory(
        private val listComponentFactory: TaskListComponent.Factory,
        private val editComponentFactory: EditTaskComponent.Factory,
        private val addComponentFactory: AddTaskComponent.Factory,
    ) : MultiPaneTasksComponent.Factory {
        override fun create(
            componentContext: ComponentContext,
        ): MultiPaneTasksComponent =
            DefaultMultiPaneTasksComponent(
                componentContext = componentContext,
                listComponentFactory = listComponentFactory,
                editComponentFactory = editComponentFactory,
                addComponentFactory = addComponentFactory,
            )
    }
}
