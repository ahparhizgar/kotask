package io.amirhparhizgar.kotask

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.amirhparhizgar.kotask.taskoperation.TaskOperationComponent

@Composable
fun Task(
    component: TaskOperationComponent,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        Text(component.task.title)
        Spacer(Modifier.weight(1f))
        Checkbox(
            checked = component.task.isDone,
            onCheckedChange = { isChecked ->
                component.setDone(isChecked)
            },
        )
    }
}
