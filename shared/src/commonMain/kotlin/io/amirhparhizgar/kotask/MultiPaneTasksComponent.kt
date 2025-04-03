package io.amirhparhizgar.kotask

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.panels.ChildPanels
import com.arkivanov.decompose.router.panels.Panels
import com.arkivanov.decompose.router.panels.PanelsNavigation
import com.arkivanov.decompose.router.panels.childPanels
import com.arkivanov.decompose.router.panels.navigate
import com.arkivanov.decompose.value.Value
import io.amirhparhizgar.kotask.list.TaskListComponent

interface MultiPaneTasksComponent {
    @OptIn(ExperimentalDecomposeApi::class)
    val panels: Value<ChildPanels<Unit, ViewSelector, Unit, TaskListComponent, EditTask, EditTaskComponent>>
    val listComponent: TaskListComponent
    val addComponent: AddTaskComponent

    fun openDetails(task: Task)
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
            initialPanels = { Panels(main = Unit, details = null, extra = null) },
            serializers = null,
            handleBackButton = true,
            mainFactory = { _, ctx -> ViewSelector() },
            detailsFactory = { _, ctx -> listComponentFactory(ctx) },
            extraFactory = { cfg, ctx -> editComponentFactory(ctx, cfg.taskId) },
        )

    override val listComponent = listComponentFactory(componentContext)
    override val addComponent = addComponentFactory(componentContext)

    override fun openDetails(task: Task) {
        navigation.navigate { state ->
            state.copy(extra = EditTask(task.id))
        }
    }
}
