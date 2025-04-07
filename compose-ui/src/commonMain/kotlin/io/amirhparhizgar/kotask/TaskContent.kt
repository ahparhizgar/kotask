package io.amirhparhizgar.kotask

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Task(
    component: TaskItemComponent,
    modifier: Modifier = Modifier,
) {
    Card(shape = MaterialTheme.shapes.small) {
        Row(
            modifier = modifier
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
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
                isStarred = component.task.isImportant,
                onStarClick = { component.setImportance(!component.task.isImportant) },
            )
        }
    }
}

@Screenshot
@Composable
@Preview
fun TaskPreview() {
    MaterialTheme {
        Task(component = FakeTaskItemComponent())
    }
}
