package io.amirhparhizgar.kotask

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.amirhparhizgar.kotask.list.FakeTaskFactory
import io.amirhparhizgar.kotask.list.Task
import io.amirhparhizgar.kotask.taskoperation.TaskOperationComponent
import kotlinx.coroutines.Job
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Task(
    component: TaskOperationComponent,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = component.task.isDone,
            onCheckedChange = { isChecked ->
                component.setDone(isChecked)
            },
        )
        Text(component.task.title)
    }
}

@Screenshot
@Composable
@Preview
fun TaskPreview() {
    MaterialTheme {
        Task(component = PreviewTaskOperationComponent())
    }
}

class PreviewTaskOperationComponent(
    override val task: Task = FakeTaskFactory.create(),
) : TaskOperationComponent {
    override fun setDone(done: Boolean): Job = Job()
}
