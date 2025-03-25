package io.amirhparhizgar.kotask

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
        CircularCheckbox(
            checked = component.task.isDone,
            onCheckedChange = { isChecked ->
                component.setDone(isChecked)
            },
        )
        Spacer(Modifier.width(8.dp))
        Text(modifier = Modifier.weight(1f), text = component.task.title)
        Spacer(Modifier.width(8.dp))
        StarButton(
            isStarred = false,
            onStarClick = { },
        )
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
