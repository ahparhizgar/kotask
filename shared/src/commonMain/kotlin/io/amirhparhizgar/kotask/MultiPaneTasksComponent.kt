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
}

data class EditTask(val taskId: String)

class ViewSelector

@OptIn(ExperimentalDecomposeApi::class)
class DefaultMultiPaneTasksComponent(
    componentContext: ComponentContext,
    private val listComponentFactory: (context: ComponentContext) -> TaskListComponent,
    private val editComponentFactory: (context: ComponentContext, id: String) -> EditTaskComponent,
    addComponentFactory: (context: ComponentContext) -> AddTaskComponent,
) : ComponentContext by componentContext, MultiPaneTasksComponent {
    private val navigation = PanelsNavigation<Unit, Unit, EditTask>()
    override val panels =
        childPanels(
            source = navigation,
            initialPanels = { Panels(main = Unit, details = Unit, extra = null) },
            serializers = null,
            handleBackButton = true,
            mainFactory = { _, ctx -> ViewSelector() },
            detailsFactory = { _, ctx -> listComponentFactory(ctx) },
            extraFactory = { cfg, ctx -> editComponentFactory(ctx, cfg.taskId) },
        )

    // todo move it to a new component containing list component
    override val addComponent = addComponentFactory(componentContext)

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
}
