package io.amirhparhizgar.kotask

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun EditTaskContent(
    editTaskComponent: EditTaskComponent,
    modifier: Modifier = Modifier,
) {
    val loadingTask by editTaskComponent.task.subscribeAsState()
    if (loadingTask is LoadingTask.NotLoaded) {
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }
    val task = (loadingTask as LoadingTask.Loaded).task
    Column(modifier = modifier.padding(16.dp)) {
        Card {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CircularCheckbox(
                    checked = task.isDone,
                    onCheckedChange = { editTaskComponent.setDone(it) },
                )
                TextField(
                    modifier = Modifier.weight(1f),
                    value = task.title,
                    onValueChange = { editTaskComponent.setTitle(it) },
                )
                StarButton(
                    isStarred = task.isImportant,
                    onStarClick = { editTaskComponent.setImportance(!task.isImportant) },
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    MaterialTheme {
        EditTaskContent(FakeEditTaskComponent())
    }
}
