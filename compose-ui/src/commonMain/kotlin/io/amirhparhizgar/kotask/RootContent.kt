package io.amirhparhizgar.kotask

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.Child
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.experimental.panels.ChildPanels
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.panels.ChildPanels
import com.arkivanov.decompose.router.panels.ChildPanelsMode
import com.arkivanov.decompose.value.MutableValue
import io.amirhparhizgar.kotask.list.FakeTaskListComponent
import io.amirhparhizgar.kotask.list.TaskListComponent
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun RootContent(
    component: MultiPaneTasksComponent,
    modifier: Modifier = Modifier,
) {
    MaterialTheme {
        Surface(modifier = modifier.fillMaxSize().windowInsetsPadding(WindowInsets.systemBars)) {
//            component.setMode(ChildPanelsMode.TRIPLE)
            val panels by component.panels.subscribeAsState()

            ChildPanels(
                panels = panels,
                mainChild = {
                    Text("Coming soon!")
                },
                detailsChild = {
                    TaskListContent(component = it.instance, addComponent = component.addComponent)
                },
                extraChild = {
                    EditTaskContent(editTaskComponent = it.instance)
                },
            )
        }
    }
}

@Screenshot
@Composable
@Preview
private fun RootPreview() {
    RootContent(
        component = FakeMultiPaneTasksComponent(),
    )
}

@OptIn(ExperimentalDecomposeApi::class)
class FakeMultiPaneTasksComponent : MultiPaneTasksComponent {
    val viewSelectorChild = Child.Created(Unit, ViewSelector())
    val taskListChild = Child.Created<Unit, TaskListComponent>(
        configuration = Unit,
        instance = FakeTaskListComponent(emptyList()),
    )
    val editTaskChild =
        Child.Created<EditTask, EditTaskComponent>(EditTask("ID8379234"), FakeEditTaskComponent())
    override val panels = MutableValue(
        ChildPanels(
            mode = ChildPanelsMode.TRIPLE,
            main = viewSelectorChild,
            details = taskListChild,
            extra = editTaskChild,
        ),
    )
    override val addComponent: AddTaskComponent = PreviewAddTaskComponent()

    override fun openDetails(task: Task) {}

    override fun setMode(mode: ChildPanelsMode) {}
}
